package com.example.carRental.models;

public enum PaymentMethod {
    CARD("Картой"),
    CASH("Наличными"),
    SBP("СБП");

    private final String description;

    PaymentMethod(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
