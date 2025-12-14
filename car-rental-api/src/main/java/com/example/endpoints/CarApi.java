package com.example.endpoints;

import com.example.dto.response.CarResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@Tag(name = "cars", description = "API для просмотра автомобилей")
@RequestMapping("/api/cars")
public interface CarApi {

    @Operation(summary = "Получить список автомобилей с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список автомобилей")
    @GetMapping
    PagedModel<EntityModel<CarResponse>> getAllCars(
            @Parameter(description = "Фильтр по бренду") @RequestParam(required = false) String brand,
            @Parameter(description = "Фильтр по модели") @RequestParam(required = false) String model,
            @Parameter(description = "Фильтр по цвету") @RequestParam(required = false) String color,
            @Parameter(description = "Фильтр по доступности") @RequestParam(required = false) Boolean available,
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Получить автомобиль по ID")
    @ApiResponse(responseCode = "200", description = "Автомобиль найден")
    @ApiResponse(responseCode = "404", description = "Автомобиль не найден")
    @GetMapping("/{id}")
    EntityModel<CarResponse> getCarById(@PathVariable String id);

}
