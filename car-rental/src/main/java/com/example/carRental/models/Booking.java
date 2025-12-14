package com.example.carRental.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    private Car car;

    private Customer customer;

    private LocalDate startDate;

    private LocalDate endDate;

    private double totalPrice;

    private Status status;

    private LocalDateTime createdAt;

    private Payment payment;



    public Booking(Car car, Customer customer, LocalDate startDate, LocalDate endDate, double totalPrice) {
        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = Status.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    protected Booking() {}

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    @Column(nullable = false)
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    @Column(nullable = false)
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    @Column(nullable = false)
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Column(nullable = false)
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
