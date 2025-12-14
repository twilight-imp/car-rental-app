package com.example.carRental.repository.impl;

import com.example.carRental.models.Payment;
import com.example.carRental.repository.PaymentRepository;

import org.springframework.stereotype.Repository;


@Repository
public class PaymentRepositoryImpl extends BaseRepositoryImpl<Payment> implements PaymentRepository {

    protected PaymentRepositoryImpl() {
        super(Payment.class);
    }
}