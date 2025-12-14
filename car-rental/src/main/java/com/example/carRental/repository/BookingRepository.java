package com.example.carRental.repository;

import com.example.carRental.models.Booking;

import java.util.List;

public interface BookingRepository extends BaseRepository<Booking> {

    List<Booking> findByCarId(String carId);
    List<Booking> findByCustomerId(String carId);

    List<Booking> findActiveBookings();

}
