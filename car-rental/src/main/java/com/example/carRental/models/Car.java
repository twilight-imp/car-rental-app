package com.example.carRental.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    private String brand;

    private String model;

    private Integer year;

    private String color;

    private String registrationPlate;

    private Integer minDailyPrice;

    private Boolean available = true;

    private List<Booking> bookings;

    public Car(String brand, String model, Integer year, String color, String registrationPlate, Integer minDailyPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.registrationPlate = registrationPlate;
        this.minDailyPrice = minDailyPrice;
    }

    protected Car() {
    }

    @Column(nullable = false)
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    @Column(nullable = false)
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    @Column(nullable = false)
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    @Column(nullable = false)
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    @Column(nullable = false, unique = true)
    public String getRegistrationPlate() { return registrationPlate; }
    public void setRegistrationPlate(String registrationPlate) { this.registrationPlate = registrationPlate; }

    @Column(nullable = false)
    public Integer getMinDailyPrice() { return minDailyPrice; }
    public void setMinDailyPrice(Integer minDailyPrice) { this.minDailyPrice = minDailyPrice; }

    @Column(nullable = false)
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @OneToMany(mappedBy = "car")
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}

