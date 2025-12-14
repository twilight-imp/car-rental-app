package org.example.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookingCreatedEvent(
        String bookingId,
        String carId,
        String customerId,
        double totalPrice,
        LocalDateTime createdAt
) implements Serializable {}
