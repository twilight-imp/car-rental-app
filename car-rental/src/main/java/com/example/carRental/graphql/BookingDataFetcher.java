package com.example.carRental.graphql;

import com.example.carRental.service.BookingService;
import com.example.dto.request.BookingRequest;
import com.example.dto.request.PaymentRequest;
import com.example.dto.response.BookingResponse;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@DgsComponent
public class BookingDataFetcher {

    private final BookingService bookingService;

    public BookingDataFetcher(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @DgsQuery
    public List<BookingResponse> bookings() {
        return bookingService.getAllBookings();
    }

    @DgsQuery
    public BookingResponse bookingById(@InputArgument String id) {
        return bookingService.getBookingById(id);
    }

    @DgsQuery
    public List<BookingResponse> bookingsByCustomerId(@InputArgument String customerId) {
        return bookingService.getBookingsByCustomerId(customerId);
    }

    @DgsQuery
    public List<BookingResponse> bookingsByCarId(@InputArgument String carId) {
        return bookingService.getBookingsByCarId(carId);
    }

    @DgsMutation
    public BookingResponse createBooking(@InputArgument("input") Map<String, Object> input) {
        BookingRequest request = new BookingRequest(
                (String) input.get("carId"),
                (String) input.get("customerId"),
                LocalDate.parse((String) input.get("startDate")),
                LocalDate.parse((String) input.get("endDate"))
        );
        return bookingService.createBooking(request);
    }

    @DgsMutation
    public BookingResponse cancelBooking(@InputArgument String id) {
        return bookingService.cancelBooking(id);
    }

    @DgsMutation
    public BookingResponse payBooking(@InputArgument String id, @InputArgument("payment") Map<String, Object> payment) {
        PaymentRequest paymentRequest = new PaymentRequest(
                ((Number) payment.get("amount")).doubleValue(),
                (String) payment.get("paymentMethod")
        );
        return bookingService.payBooking(id, paymentRequest);
    }
}