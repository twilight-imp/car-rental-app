package com.example.carRental.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(CarController.class).getAllCars(null, null, null, null, 0, 10)).withRel("cars"),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("bookings"),

                linkTo(methodOn(RootController.class).getRoot()).slash("swagger-ui.html").withRel("documentation")
        );
        return rootModel;
    }
}

