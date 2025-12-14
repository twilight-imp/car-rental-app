package com.example.carRental.repository.impl;

import com.example.carRental.models.Customer;
import com.example.carRental.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


@Repository
public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer> implements CustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected CustomerRepositoryImpl() {
        super(Customer.class);
    }

}
