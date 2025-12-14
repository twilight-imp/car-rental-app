package com.example.carRental.assembler;

import com.example.dto.response.CustomerResponse;
import com.example.endpoints.BookingApi;
import com.example.endpoints.CustomerApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<CustomerResponse, EntityModel<CustomerResponse>> {

    @Override
    public EntityModel<CustomerResponse> toModel(CustomerResponse customer) {
        return EntityModel.of(customer,
                linkTo(methodOn(CustomerApi.class).getCustomerById(customer.getId())).withSelfRel(),
                linkTo(methodOn(BookingApi.class).getAllBookings()).withRel("bookings"),
                linkTo(methodOn(CustomerApi.class).getAllCustomers()).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<CustomerResponse>> toCollectionModel(Iterable<? extends CustomerResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(CustomerApi.class).getAllCustomers()).withSelfRel());
    }
}
