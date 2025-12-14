package com.example.carRental.controllers;

import com.example.carRental.assembler.CustomerModelAssembler;
import com.example.carRental.service.CustomerService;
import com.example.dto.response.CustomerResponse;
import com.example.endpoints.CustomerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomerApi {

    private CustomerService customerService;
    private CustomerModelAssembler customerModelAssembler;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCustomerModelAssembler(CustomerModelAssembler customerModelAssembler) {
        this.customerModelAssembler = customerModelAssembler;
    }

    @Override
    public CollectionModel<EntityModel<CustomerResponse>> getAllCustomers() {
        return customerModelAssembler.toCollectionModel(customerService.getAllCustomers());
    }

    @Override
    public EntityModel<CustomerResponse> getCustomerById(String id) {
        return customerModelAssembler.toModel(customerService.getCustomerById(id));
    }
}