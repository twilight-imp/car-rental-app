package com.example.carRental.service;

import com.example.carRental.models.Car;
import com.example.carRental.repository.BookingRepository;
import com.example.carRental.repository.CarRepository;

import com.example.dto.response.CarResponse;
import com.example.dto.response.PagedResponse;
import com.example.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private CarRepository carRepository;
    private BookingRepository bookingRepository;
    private ModelMapper modelMapper;



    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Autowired
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    public List<CarResponse> getAllCars() {
        return carRepository.getAll().stream()
                .map(car -> modelMapper.map(car, CarResponse.class))
                .collect(Collectors.toList());
    }

    public PagedResponse<CarResponse> getAllCars(String brand, String model, String color, Boolean available, int page, int size) {
        List<Car> allCars = carRepository.getAll();

        List<Car> filteredCars = allCars.stream()
                .filter(car -> brand == null || car.getBrand().equalsIgnoreCase(brand))
                .filter(car -> model == null || car.getModel().equalsIgnoreCase(model))
                .filter(car -> color == null || car.getColor().equalsIgnoreCase(color))
                .filter(car -> available == null || car.isAvailable() == available)
                .toList();

        int totalElements = filteredCars.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = page * size;

        List<CarResponse> pageContent = filteredCars.stream()
                .skip(fromIndex)
                .limit(size)
                .map(car -> modelMapper.map(car, CarResponse.class))
                .collect(Collectors.toList());

        return new PagedResponse<>(pageContent, page, size, totalElements, totalPages, page >= totalPages - 1);
    }
    public CarResponse getCarById(String id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Автомобиль", id));
        return modelMapper.map(car, CarResponse.class);
    }
}