package com.example.notification_service.rabbitmq;

import com.example.notification_service.websocket.NotificationHandler;
import org.example.events.BookingCancelledEvent;
import org.example.events.BookingCreatedEvent;
import org.example.events.BookingPaidEvent;
import org.example.events.PriceCalculatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final NotificationHandler notificationHandler;

    public NotificationListener(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.booking.created", durable = "true"),
                    exchange = @Exchange(name = "car-rental-exchange", type = "topic"),
                    key = "booking.created"
            )
    )
    public void handleBookingCreatedEvent(BookingCreatedEvent event) {
        log.info("Received event from RabbitMQ: {}", event);

        String userMessage = String.format(
                "{\"type\": \"BOOKING_CREATED\", \"bookingId\": \"%s\", \"carId\": \"%s\", \"customerId\": \"%s\"}",
                event.bookingId(),
                event.carId(),
                event.customerId()
        );

        // Отправляем в браузер
        notificationHandler.broadcast(userMessage);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.booking.paid", durable = "true"),
                    exchange = @Exchange(name = "car-rental-exchange", type = "topic"),
                    key = "booking.paid"
            )
    )
    public void handleBookingPaidEvent(BookingPaidEvent event) {
        log.info("Received event from RabbitMQ: {}", event);

        String userMessage = String.format(
                "{\"type\": \"BOOKING_PAID\", \"bookingId\": \"%s\", \"paymentMethod\": \"%s\", \"amount\": \"%s руб.\"}",
                event.bookingId(),
                event.paymentMethod(),
                event.amount()
        );

        notificationHandler.broadcast(userMessage);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.booking.cancelled", durable = "true"),
                    exchange = @Exchange(name = "car-rental-exchange", type = "topic"),
                    key = "booking.cancelled"
            )
    )
    public void handleBookingCancelledEvent(BookingCancelledEvent event) {
        log.info("Received event from RabbitMQ: {}", event);

        String userMessage = String.format(
                "{\"type\": \"BOOKING_CANCELLED\", \"bookingId\": \"%s\", \"date\": \"%s\"}",
                event.bookingId(),
                event.cancelledAt()
        );

        notificationHandler.broadcast(userMessage);
    }

    // Дополнительно: слушаем события из fanout exchange
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.pricing", durable = "true"),
                    exchange = @Exchange(name = "price-calculated-fanout", type = "fanout")
            )
    )
    public void handlePriceCalculatedEvent(PriceCalculatedEvent event) {
        log.info("Received price calculated event: {}", event);

        String userMessage = String.format(
                "{\"type\": \"PRICE_CALCULATED\", \"bookingId\": \"%s\", \"price\": %s}",
                event.bookingId(),
                event.price()
        );

        notificationHandler.broadcast(userMessage);
    }
}