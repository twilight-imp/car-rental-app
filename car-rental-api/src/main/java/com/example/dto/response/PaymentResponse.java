package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "payments", itemRelation = "payment")
public class PaymentResponse extends RepresentationModel<PaymentResponse> {

    private  String id;
    private  Double amount;
    private  String paymentMethod;
    private  LocalDateTime createdAt;


    public PaymentResponse(String id, Double amount, String paymentMethod,LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected PaymentResponse() {
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PaymentResponse that = (PaymentResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(amount, that.amount) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, amount, paymentMethod, createdAt);
    }
}