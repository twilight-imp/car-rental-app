package com.example.dto.response;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(collectionRelation = "cars", itemRelation = "car")
public class CarResponse extends RepresentationModel<CarResponse> {

    private  String id;
    private  String brand;
    private  String model;
    private  Integer year;
    private  String color;
    private  String registrationPlate;
    private  Integer minDailyPrice;
    private  boolean available;

    public CarResponse(String id, String brand, String model, Integer year, String color, String registrationPlate, Integer minDailyPrice, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.registrationPlate = registrationPlate;
        this.minDailyPrice = minDailyPrice;
        this.available = available;
    }

    protected CarResponse() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public void setMinDailyPrice(Integer minDailyPrice) {
        this.minDailyPrice = minDailyPrice;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public Integer getYear() { return year; }
    public String getColor() { return color; }
    public String getRegistrationPlate() { return registrationPlate; }
    public Integer getMinDailyPrice() { return minDailyPrice; }
    public boolean isAvailable() { return available; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CarResponse that = (CarResponse) o;
        return available == that.available &&
                Objects.equals(id, that.id) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(model, that.model) &&
                Objects.equals(year, that.year) &&
                Objects.equals(color, that.color) &&
                Objects.equals(registrationPlate, that.registrationPlate) &&
                Objects.equals(minDailyPrice, that.minDailyPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, brand, model, year, color, registrationPlate, minDailyPrice, available);
    }
}