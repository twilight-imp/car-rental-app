package org.example.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookingPaidEvent(
        String bookingId,
        double amount,
        String paymentMethod,
        LocalDateTime paidAt
) implements Serializable {}
