package com.example.carRental.models;

public enum Status {
    CREATED("Создана"),
    CONFIRMED("Подтверждена"),
    CANCELLED("Отменена клиентом"),

    EXPIRED("Истек срок оплаты"),
    COMPLETED("Завершена");

    private final String description;

    Status(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
