package com.example.carRental.service;

import com.example.carRental.config.RabbitMQConfig;
import com.example.carRental.models.*;
import com.example.carRental.repository.BookingRepository;
import com.example.carRental.repository.CarRepository;
import com.example.carRental.repository.CustomerRepository;
import com.example.carRental.repository.PaymentRepository;

import com.example.dto.request.BookingRequest;
import com.example.dto.request.PaymentRequest;
import com.example.dto.response.BookingResponse;
import com.example.exception.CarNotAvailableException;
import com.example.exception.InvalidBookingDatesException;
import com.example.exception.InvalidBookingStatusException;
import com.example.exception.ResourceNotFoundException;
import grpc.pricing.CalculatePriceRequest;
import grpc.pricing.PricingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.events.BookingCancelledEvent;
import org.example.events.BookingCreatedEvent;
import org.example.events.BookingPaidEvent;
import org.example.events.PriceCalculatedEvent;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private CustomerRepository customerRepository;
    private PaymentRepository paymentRepository;
    private ModelMapper modelMapper;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private void setCustomerRepository(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Autowired
    private void setCarRepository(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    @Autowired
    private void setBookingRepository(BookingRepository bookingRepository){
        this.bookingRepository = bookingRepository;
    }

    @Autowired
    private void setPaymentRepository(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @GrpcClient("pricing-service")
    private PricingServiceGrpc.PricingServiceBlockingStub pricingStub;


    public List<BookingResponse> getAllBookings() {
        checkBookings();
        return bookingRepository.getAll().stream()
                .map(booking -> modelMapper.map(booking, BookingResponse.class))
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(String id) {
        checkBooking(id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование", id));
        return modelMapper.map(booking, BookingResponse.class);
    }

    public List<BookingResponse> getBookingsByCustomerId(String customerId) {
        checkBookingsForCustomer(customerId);

        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("Бронирования для клиента", customerId);
        }
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingResponse.class))
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByCarId(String carId) {
        checkBookingsForCar(carId);

        List<Booking> bookings = bookingRepository.findByCarId(carId);
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("Бронирования для автомобиля", carId);
        }
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        Car car = carRepository.findById(request.carId()).orElseThrow(() -> new ResourceNotFoundException("Автомобиль", request.carId()));

        Customer customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new ResourceNotFoundException("Клиент", request.customerId()));

        if (!car.isAvailable()) {
            throw new CarNotAvailableException("Автомобиль не доступен для брони");
        }

        if (request.startDate().isAfter(request.endDate())) {
            throw new InvalidBookingDatesException("Дата начала бронирования не может быть позже даты окончания");
        }

        if (request.startDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new InvalidBookingDatesException("Дата начала бронирования должна быть не раньше завтрашнего дня");
        }

        int rentalDays = Period.between(request.startDate(), request.endDate()).getDays() + 1;

        int age = Period.between(customer.getBirthday(), LocalDate.now()).getYears();

        var grpcRequest = CalculatePriceRequest.newBuilder().setCarBasePrice(car.getMinDailyPrice()).setRentalDays(rentalDays).setCustomerAge(age).setDrivingExperience(customer.getDrivingExperience()).build();

        var grpcResponse = pricingStub.calculateTotalPrice(grpcRequest);

        double price = grpcResponse.getFinalPrice();

        Booking booking = new Booking(car, customer, request.startDate(), request.endDate(), price);

        car.setAvailable(false);
        carRepository.update(car);
        Booking savedBooking = bookingRepository.create(booking);

        // Отправка события в Fanout
        var pricingEvent = new PriceCalculatedEvent(booking.getId(), grpcResponse.getFinalPrice());


        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", pricingEvent);


        BookingCreatedEvent event = new BookingCreatedEvent(
                savedBooking.getId(),
                savedBooking.getCar().getId(),
                savedBooking.getCustomer().getId(),
                savedBooking.getTotalPrice(),
                savedBooking.getCreatedAt()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_BOOKING_CREATED, event);

        return modelMapper.map(savedBooking, BookingResponse.class);
    }

    @Transactional
    public BookingResponse cancelBooking(String id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Бронирование", id));

        checkBooking(id);
        booking = bookingRepository.findById(id).get();

        if (booking.getStatus() == Status.CANCELLED) {
            throw new InvalidBookingStatusException("Бронирование уже отменено");
        }

        if (booking.getStatus() == Status.COMPLETED) {
            throw new InvalidBookingStatusException("Нельзя отменить завершенное бронирование");
        }

        if (booking.getStatus() == Status.EXPIRED) {
            throw new InvalidBookingStatusException("Нельзя отменить просроченное бронирование");
        }

        if (booking.getStatus() != Status.CREATED && booking.getStatus() != Status.CONFIRMED) {
            throw new InvalidBookingStatusException("Можно отменить только бронирование со статусом 'Создано' или 'Подтверждено'");
        }

        Car car = booking.getCar();
        car.setAvailable(true);
        carRepository.update(car);

        booking.setStatus(Status.CANCELLED);
        Booking updatedBooking = bookingRepository.update(booking);

        BookingCancelledEvent event = new BookingCancelledEvent(
                updatedBooking.getId(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_BOOKING_CANCELLED, event);
        return modelMapper.map(updatedBooking, BookingResponse.class);
    }

    @Transactional
    public BookingResponse payBooking(String id, PaymentRequest paymentRequest) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Бронирование", id));

        checkBooking(id);
        booking = bookingRepository.findById(id).get();

        if (booking.getStatus() == Status.COMPLETED) {
            throw new InvalidBookingStatusException("Нельзя оплатить завершенное бронирование");
        }

        if (booking.getStatus() == Status.EXPIRED) {
            throw new InvalidBookingStatusException("Нельзя оплатить просроченное бронирование");
        }

        if (booking.getStatus() != Status.CREATED) {
            throw new InvalidBookingStatusException("Можно оплатить только бронирование со статусом 'Создано'. Текущий статус: " + booking.getStatus());
        }

        if (Math.abs(paymentRequest.amount() - booking.getTotalPrice()) > 0.01) {
            throw new IllegalArgumentException("Сумма оплаты не совпадает с суммой бронирования");
        }

        if (booking.getPayment() != null) {
            throw new InvalidBookingStatusException("Бронирование уже оплачено");
        }

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(paymentRequest.paymentMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный метод оплаты: " + paymentRequest.paymentMethod());
        }

        Payment payment = new Payment(booking, paymentRequest.amount(), paymentMethod);
        Payment savedPayment = paymentRepository.create(payment);
        booking.setPayment(savedPayment);
        booking.setStatus(Status.CONFIRMED);

        Booking updatedBooking = bookingRepository.update(booking);

        BookingPaidEvent event = new BookingPaidEvent(
                updatedBooking.getId(),
                updatedBooking.getPayment().getAmount(),
                updatedBooking.getPayment().getPaymentMethod().getDescription(),
                updatedBooking.getPayment().getCreatedAt()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_BOOKING_PAID,
                event
        );
        return modelMapper.map(updatedBooking, BookingResponse.class);
    }

    @Transactional
    public void checkBookings() {
        List<Booking> activeBookings = bookingRepository.findActiveBookings();
        for (Booking booking : activeBookings) {
            if (isBookingExpired(booking)) expireBooking(booking);
            if (isBookingCompleted(booking)) completeBooking(booking);
        }
    }

    private void checkBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            if (isBookingExpired(booking) && booking.getStatus() == Status.CREATED) expireBooking(booking);
            if (isBookingCompleted(booking) && booking.getStatus() == Status.CONFIRMED) completeBooking(booking);
        }
    }

    private void checkBookingsForCustomer(String customerId) {
        List<Booking> customerBookings = bookingRepository.findByCustomerId(customerId);
        for (Booking booking : customerBookings) {
            if (isBookingExpired(booking) && booking.getStatus() == Status.CREATED) expireBooking(booking);
            if (isBookingCompleted(booking) && booking.getStatus() == Status.CONFIRMED) completeBooking(booking);
        }
    }

    private void checkBookingsForCar(String carId) {
        List<Booking> carBookings = bookingRepository.findByCarId(carId);
        for (Booking booking : carBookings) {
            if (isBookingExpired(booking) && booking.getStatus() == Status.CREATED) expireBooking(booking);
            if (isBookingCompleted(booking) && booking.getStatus() == Status.CONFIRMED) completeBooking(booking);
        }
    }

    private boolean isBookingExpired(Booking booking) {
        return booking.getStartDate().isBefore(LocalDate.now()) && booking.getStatus() == Status.CREATED;
    }

    private boolean isBookingCompleted(Booking booking) {
        return booking.getEndDate().isBefore(LocalDate.now()) && booking.getStatus() == Status.CONFIRMED;
    }
    private void expireBooking(Booking booking) {
        Car car = booking.getCar();
        car.setAvailable(true);
        carRepository.update(car);

        booking.setStatus(Status.EXPIRED);
        bookingRepository.update(booking);
    }

    private void completeBooking(Booking booking) {
        Car car = booking.getCar();
        car.setAvailable(true);
        carRepository.update(car);

        booking.setStatus(Status.COMPLETED);
        bookingRepository.update(booking);
    }

    private double countPrice(LocalDate startDay, LocalDate endDay) {
        Period period = Period.between(startDay, endDay);
        Random random = new Random();
        double priceOneDay = random.nextDouble(5000, 10000);
        double total = priceOneDay * (period.getDays() + 1);
        return Math.round(total * 100.0) / 100.0;
    }
}