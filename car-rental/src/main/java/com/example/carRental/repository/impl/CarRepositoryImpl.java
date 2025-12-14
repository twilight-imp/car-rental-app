package com.example.carRental.repository.impl;

import com.example.carRental.models.Car;
import com.example.carRental.repository.CarRepository;
import org.springframework.stereotype.Repository;


@Repository
public class CarRepositoryImpl extends BaseRepositoryImpl<Car> implements CarRepository {
    protected CarRepositoryImpl() {
        super(Car.class);
    }
}
