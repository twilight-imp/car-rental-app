package com.example.carRental.graphql;

import com.example.carRental.service.BookingService;
import com.example.carRental.service.CarService;
import com.example.dto.response.BookingResponse;
import com.example.dto.response.CarResponse;
import com.example.dto.response.PagedResponse;
import com.example.exception.ResourceNotFoundException;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class CarDataFetcher {

    private CarService carService;

    private BookingService bookingService;

    @Autowired
    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Autowired
    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    @DgsQuery
    public PagedResponse<CarResponse> cars(
            @InputArgument String brand,
            @InputArgument String model,
            @InputArgument String color,
            @InputArgument Boolean available,
            @InputArgument Integer page,
            @InputArgument Integer size) {

        return carService.getAllCars(brand, model,color, available, page, size);
    }

    @DgsData(parentType = "Car", field = "bookings")
    public List<BookingResponse> bookings(DataFetchingEnvironment dfe) {
        CarResponse car = dfe.getSource();

        if(car == null || car.getId() == null){
            return List.of();
        }

        try {
            return bookingService.getBookingsByCarId(car.getId());
        }catch (ResourceNotFoundException e){
            return List.of();
        }
    }



    @DgsQuery
    public CarResponse carById(@InputArgument String id) {
        return carService.getCarById(id);
    }

}