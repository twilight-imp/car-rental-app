package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "bookings", itemRelation = "booking")
public class BookingResponse extends RepresentationModel<BookingResponse> {

    private  String id;
    private  CarResponse car;
    private  CustomerResponse customer;
    private  LocalDate startDate;
    private  LocalDate endDate;
    private  double totalPrice;
    private  String status;
    private  LocalDateTime createdAt;

    private PaymentResponse payment;


    //конструкторы, геттеры, сеттеры, equals и hashCode()
    public BookingResponse(String id, CarResponse car, CustomerResponse customer, LocalDate startDate, LocalDate endDate, double totalPrice, String status, LocalDateTime createdAt, PaymentResponse payment) {
        this.id = id;
        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.payment = payment;
    }

    protected BookingResponse(){}

    public void setId(String id) {
        this.id = id;
    }

    public void setCar(CarResponse car) {
        this.car = car;
    }

    public void setCustomer(CustomerResponse customer) {
        this.customer = customer;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    public String getId() { return id; }
    public CarResponse getCar() { return car; }
    public CustomerResponse getCustomer() { return customer; }

    @JsonFormat(pattern = "dd-MM-yyyy")
    public LocalDate getStartDate() { return startDate; }

    @JsonFormat(pattern = "dd-MM-yyyy")
    public LocalDate getEndDate() { return endDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    public LocalDateTime getCreatedAt() { return createdAt; }

    public PaymentResponse getPayment() {
        return payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookingResponse that = (BookingResponse) o;
        return Double.compare(that.totalPrice, totalPrice) == 0 && Objects.equals(id, that.id) && Objects.equals(car, that.car) && Objects.equals(customer, that.customer) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(status, that.status) && Objects.equals(createdAt, that.createdAt) && Objects.equals(payment, that.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, car, customer, startDate, endDate, totalPrice, status, createdAt, payment);
    }
}