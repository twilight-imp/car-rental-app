package com.example.carRental.listeners;

import org.example.events.PriceCalculatedEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class InternalPricingListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.car-rental.pricing.log", durable = "true"),
                    exchange = @Exchange(name = "price-calculated-fanout", type = "fanout")
            )
    )
    public void handleRating(PriceCalculatedEvent event) {
        System.out.println("Рассчитана стоимость бронирования " + event.bookingId());
    }
}

