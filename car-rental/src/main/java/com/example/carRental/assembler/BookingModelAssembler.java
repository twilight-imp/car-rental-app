package com.example.carRental.assembler;

import com.example.carRental.models.Status;
import com.example.dto.response.BookingResponse;
import com.example.endpoints.BookingApi;
import com.example.endpoints.CarApi;
import com.example.endpoints.CustomerApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BookingModelAssembler implements RepresentationModelAssembler<BookingResponse, EntityModel<BookingResponse>> {

    @Override
    public EntityModel<BookingResponse> toModel(BookingResponse booking) {
        EntityModel<BookingResponse> model = EntityModel.of(booking,
                linkTo(methodOn(BookingApi.class).getBookingById(booking.getId())).withSelfRel(),
                linkTo(methodOn(CarApi.class).getCarById(booking.getCar().getId())).withRel("car"),
                linkTo(methodOn(CustomerApi.class).getCustomerById(booking.getCustomer().getId())).withRel("customer"),
                linkTo(methodOn(BookingApi.class).getAllBookings()).withRel("collection")
        );

        if (booking.getStatus().equals(Status.CREATED.toString())) {
            model.add(linkTo(methodOn(BookingApi.class).payBooking(booking.getId(), null)).withRel("pay"));
        }

        if (!(booking.getStatus().equals(Status.CANCELLED.toString())) && !(booking.getStatus().equals(Status.COMPLETED.toString())) && !(booking.getStatus().equals(Status.EXPIRED.toString()))) {
            model.add(linkTo(methodOn(BookingApi.class).cancelBooking(booking.getId())).withRel("cancel"));
        }
        return model;
    }

    @Override
    public CollectionModel<EntityModel<BookingResponse>> toCollectionModel(Iterable<? extends BookingResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(BookingApi.class).getAllBookings()).withSelfRel());
    }
}