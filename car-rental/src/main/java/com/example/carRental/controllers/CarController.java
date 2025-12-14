package com.example.carRental.controllers;

import com.example.carRental.assembler.CarModelAssembler;
import com.example.carRental.service.CarService;
import com.example.dto.response.CarResponse;
import com.example.dto.response.PagedResponse;
import com.example.endpoints.CarApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController implements CarApi {

    private CarService carService;
    private CarModelAssembler carModelAssembler;
    private PagedResourcesAssembler<CarResponse> pagedResourcesAssembler;

    @Autowired
    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    @Autowired
    public void setCarModelAssembler(CarModelAssembler carModelAssembler) {
        this.carModelAssembler = carModelAssembler;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<CarResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }
    @Override
    public PagedModel<EntityModel<CarResponse>> getAllCars(String brand, String model, String color, Boolean available, int page, int size) {
        PagedResponse<CarResponse> pagedResponse = carService.getAllCars(brand, model, color, available, page, size);

        Page<CarResponse> carPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(carPage, carModelAssembler);
    }

    @Override
    public EntityModel<CarResponse> getCarById(String id) {
        return carModelAssembler.toModel(carService.getCarById(id));
    }
}
