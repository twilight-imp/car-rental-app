package com.example.carRental.controllers;

import com.example.carRental.assembler.BookingModelAssembler;
import com.example.carRental.service.BookingService;
import com.example.dto.request.BookingRequest;
import com.example.dto.request.PaymentRequest;
import com.example.dto.response.BookingResponse;
import com.example.endpoints.BookingApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController implements BookingApi {

    private BookingService bookingService;

    private BookingModelAssembler bookingModelAssembler;

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Autowired
    public void setBookingModelAssembler(BookingModelAssembler bookingModelAssembler) {
        this.bookingModelAssembler = bookingModelAssembler;
    }

    @Override
    public CollectionModel<EntityModel<BookingResponse>> getAllBookings() {
        return bookingModelAssembler.toCollectionModel(bookingService.getAllBookings());
    }

    @Override
    public EntityModel<BookingResponse> getBookingById(String id) {
        return bookingModelAssembler.toModel(bookingService.getBookingById(id));
    }

    @Override
    public CollectionModel<EntityModel<BookingResponse>> getBookingsByCustomerId(String customerId) {
        List<BookingResponse> bookings = bookingService.getBookingsByCustomerId(customerId);
        return bookingModelAssembler.toCollectionModel(bookings);
    }

    @Override
    public CollectionModel<EntityModel<BookingResponse>> getBookingsByCarId(String carId) {
        List<BookingResponse> bookings = bookingService.getBookingsByCarId(carId);
        return bookingModelAssembler.toCollectionModel(bookings);
    }
    @Override
    public ResponseEntity<EntityModel<BookingResponse>> createBooking(BookingRequest request) {
        BookingResponse booking = bookingService.createBooking(request);
        EntityModel<BookingResponse> model = bookingModelAssembler.toModel(booking);
        return ResponseEntity.created(model.getRequiredLink("self").toUri()).body(model);
    }

    @Override
    public EntityModel<BookingResponse> payBooking(String id, PaymentRequest paymentRequest) {
        return bookingModelAssembler.toModel(bookingService.payBooking(id, paymentRequest));
    }

    @Override
    public EntityModel<BookingResponse> cancelBooking(String id) {
        return bookingModelAssembler.toModel(bookingService.cancelBooking(id));
    }
}