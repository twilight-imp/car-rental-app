package com.example.carRental.config;

import com.example.carRental.models.*;
import com.example.carRental.repository.CarRepository;
import com.example.carRental.repository.CustomerRepository;
import com.example.dto.response.BookingResponse;
import com.example.dto.response.PaymentResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(CarRepository carRepository, CustomerRepository customerRepository) {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Status, String> toDescription = ctx -> ctx.getSource() == null ? null: ctx.getSource().getDescription();
        Converter<PaymentMethod, String> toDescriptionPay = ctx -> ctx.getSource() == null ? null: ctx.getSource().getDescription();

        modelMapper.addMappings(new PropertyMap<Booking, BookingResponse>() {
            @Override
            protected void configure() {
                using(toDescription).map(source.getStatus(), destination.getStatus());
            }
        });

        modelMapper.addMappings(new PropertyMap<Payment, PaymentResponse>() {
            @Override
            protected void configure() {
                using(toDescriptionPay).map(source.getPaymentMethod(), destination.getPaymentMethod());
            }
        });

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        return modelMapper;
    }
}