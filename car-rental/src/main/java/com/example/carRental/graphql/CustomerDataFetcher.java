package com.example.carRental.graphql;

import com.example.carRental.service.BookingService;
import com.example.carRental.service.CustomerService;
import com.example.dto.response.BookingResponse;
import com.example.dto.response.CustomerResponse;
import com.example.exception.ResourceNotFoundException;
import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class CustomerDataFetcher {

    private CustomerService customerService;

    private BookingService bookingService;

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @DgsQuery
    public List<CustomerResponse> customers() {
        return customerService.getAllCustomers();
    }

    @DgsData(parentType = "Customer", field = "bookings")
    public List<BookingResponse> bookings(DataFetchingEnvironment dfe) {
        CustomerResponse customer = dfe.getSource();

        if (customer == null || customer.getId() == null) {
            return List.of();
        }

        try {
            return bookingService.getBookingsByCustomerId(customer.getId());
        } catch (ResourceNotFoundException e) {
            return List.of();
        }
    }

    @DgsQuery
    public CustomerResponse customerById(@InputArgument String id) {
        return customerService.getCustomerById(id);
    }
}