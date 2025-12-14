package org.example.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookingCancelledEvent(
        String bookingId,
        LocalDateTime cancelledAt
) implements Serializable {}
