package com.example.analytics_service.listeners;

import org.example.events.BookingCancelledEvent;
import org.example.events.BookingCreatedEvent;
import org.example.events.BookingPaidEvent;
import org.example.events.PriceCalculatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class BookingEventListener {

    private static final Logger log = LoggerFactory.getLogger(BookingEventListener.class);

    private final Set<String> processedEvents = ConcurrentHashMap.newKeySet();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "booking-created-queue", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                            @Argument(name = "x-dead-letter-routing-key", value = "dlq.analytics")
                    }
            ),
            exchange = @Exchange(name = "car-rental-exchange", type = "topic", durable = "true"),
            key = "booking.created"
    ))

    public void handleBookingCreatedEvent(@Payload BookingCreatedEvent event,
                                          Channel channel,
                                          @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        String eventId = "created_" + event.bookingId();

        try {
            if (!processedEvents.add(eventId)) {
                log.warn("Duplicate BookingCreatedEvent received for bookingId: {}", event.bookingId());
                channel.basicAck(deliveryTag, false);
                return;
            }

            log.info("Создана новая бронь: {}", event);

            if (event.carId() != null && event.carId().equalsIgnoreCase("CRASH")) {
                throw new RuntimeException("Simulating processing error for DLQ test");
            }

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process BookingCreatedEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "booking-paid-queue", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                            @Argument(name = "x-dead-letter-routing-key", value = "dlq.analytics")
                    }
            ),
            exchange = @Exchange(name = "car-rental-exchange", type = "topic", durable = "true"),
            key = "booking.paid"
    ))

    public void handleBookingPaidEvent(@Payload BookingPaidEvent event,
                                       Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        String eventId = "paid_" + event.bookingId();
        try {
            if (!processedEvents.add(eventId)) {
                log.warn("Duplicate BookingPaidEvent received for bookingId: {}", event.bookingId());
                channel.basicAck(deliveryTag, false);
                return;
            }

            log.info("Произведена новая оплата: {}", event);

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process BookingPaidEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "booking-cancelled-queue", durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                            @Argument(name = "x-dead-letter-routing-key", value = "dlq.analytics")
                    }
            ),
            exchange = @Exchange(name = "car-rental-exchange", type = "topic", durable = "true"),
            key = "booking.cancelled"
    ))

    public void handleBookingCancelledEvent(@Payload BookingCancelledEvent event,
                                            Channel channel,
                                            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        String eventId = "cancelled_" + event.bookingId();
        try {
            if (!processedEvents.add(eventId)) {
                log.warn("Duplicate BookingCancelledEvent received for bookingId: {}", event.bookingId());
                channel.basicAck(deliveryTag, false);
                return;
            }

            log.info("Отменена бронь: {}", event);

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process BookingCancelledEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
    
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "analytics-queue.dlq", durable = "true"),
            exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
            key = "dlq.analytics"
    ))
    public void handleDlqMessages(Object failedMessage) {
        log.error("!!! Received message in DLQ: {}", failedMessage);

    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.analytics.pricing", durable = "true"),
                    exchange = @Exchange(name = "price-calculated-fanout", type = "fanout")
            )
    )
    public void handlePrice(PriceCalculatedEvent event) {
        log.info("NOTIFY: Рассчитана стоимость для бронирования {} = {}руб.", event.bookingId(), event.price());
    }

}



