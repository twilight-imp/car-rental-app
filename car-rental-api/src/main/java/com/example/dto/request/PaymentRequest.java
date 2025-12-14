package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;


public record PaymentRequest(
    @NotBlank(message = "Сумма не может быть пустой")
    Double amount,
    @NotBlank(message = "Способ оплаты не может быть пустым")
    String paymentMethod

){}