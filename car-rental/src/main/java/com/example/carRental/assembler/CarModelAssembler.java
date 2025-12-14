package com.example.carRental.assembler;

import com.example.dto.response.CarResponse;
import com.example.endpoints.BookingApi;
import com.example.endpoints.CarApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarModelAssembler implements RepresentationModelAssembler<CarResponse, EntityModel<CarResponse>> {

    @Override
    public EntityModel<CarResponse> toModel(CarResponse car) {
        return EntityModel.of(car,
                linkTo(methodOn(CarApi.class).getCarById(car.getId())).withSelfRel(),
                linkTo(methodOn(BookingApi.class).getBookingsByCarId(car.getId())).withRel("bookings"),
                linkTo(methodOn(CarApi.class).getAllCars(null, null, null, null, 0, 10)).withRel("collection")
        );
    }



    @Override
    public CollectionModel<EntityModel<CarResponse>> toCollectionModel(Iterable<? extends CarResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(CarApi.class).getAllCars(null, null, null, null, 0, 10)).withSelfRel());
    }
}
