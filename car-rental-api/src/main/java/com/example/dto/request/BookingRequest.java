package com.example.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
public record BookingRequest(
        @NotNull(message = "ID автомобиля обязателен")
        String carId,

        @NotNull(message = "ID клиента обязателен")
        String customerId,

        @NotNull(message = "Дата начала аренды обязательна")
        @Future(message = "Дата начала должна быть в будущем")
        LocalDate startDate,

        @NotNull(message = "Дата окончания аренды обязательна")
        @Future(message = "Дата окончания должна быть в будущем")
        LocalDate endDate
) {}