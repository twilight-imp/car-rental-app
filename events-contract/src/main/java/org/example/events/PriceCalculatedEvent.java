package org.example.events;

import java.io.Serializable;

public record PriceCalculatedEvent(String bookingId, double price) implements Serializable {}
