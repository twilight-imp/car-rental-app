package com.example.endpoints;

import com.example.dto.request.BookingRequest;
import com.example.dto.request.PaymentRequest;
import com.example.dto.response.BookingResponse;
import com.example.dto.response.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "bookings", description = "API для бронирований автомобилей")
@RequestMapping("/api/bookings")
public interface BookingApi {

    @Operation(summary = "Получить список бронирований")
    @ApiResponse(responseCode = "200", description = "Список бронирований")
    @GetMapping
    CollectionModel<EntityModel<BookingResponse>> getAllBookings();

    @Operation(summary = "Получить бронирование по ID")
    @ApiResponse(responseCode = "200", description = "Бронирование найдено")
    @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    @GetMapping("/{id}")
    EntityModel<BookingResponse> getBookingById(@PathVariable String id);

    @Operation(summary = "Получить бронирования по ID клиента")
    @ApiResponse(responseCode = "200", description = "Бронирования найдены")
    @ApiResponse(responseCode = "404", description = "Бронирования не найдены")
    @GetMapping("/customer/{customerId}")
    CollectionModel<EntityModel<BookingResponse>> getBookingsByCustomerId(@PathVariable String customerId);

    @Operation(summary = "Получить бронирования по ID автомобиля")
    @ApiResponse(responseCode = "200", description = "Бронирования найдены")
    @ApiResponse(responseCode = "404", description = "Бронирования не найдены")
    @GetMapping("/car/{carId}")
    CollectionModel<EntityModel<BookingResponse>> getBookingsByCarId(@PathVariable String carId);

    @Operation(summary = "Создать новое бронирование")
    @ApiResponse(responseCode = "201", description = "Бронирование успешно создано")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<BookingResponse>>  createBooking(@Valid @RequestBody BookingRequest request);

    @Operation(summary = "Оплатить бронирование")
    @ApiResponse(responseCode = "200", description = "Бронирование оплачено")
    @PostMapping("/{id}/pay")
    EntityModel<BookingResponse> payBooking(@PathVariable String id, @RequestBody PaymentRequest paymentRequest);

    @Operation(summary = "Отменить бронирование")
    @ApiResponse(responseCode = "200", description = "Бронирование отменено")
    @PostMapping("/{id}/cancel")
    EntityModel<BookingResponse> cancelBooking(@PathVariable String id);

}
