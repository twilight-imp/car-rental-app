package com.example.endpoints;

import com.example.dto.response.CustomerResponse;
import com.example.dto.response.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@Tag(name = "customers", description = "API для управления клиентами")
@RequestMapping("/api/customers")
public interface CustomerApi {

    @Operation(summary = "Получить список клиентов")
    @ApiResponse(responseCode = "200", description = "Список клиентов")
    @GetMapping
    CollectionModel<EntityModel<CustomerResponse>>  getAllCustomers();

    @Operation(summary = "Получить клиента по ID")
    @ApiResponse(responseCode = "200", description = "Клиент найден")
    @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<CustomerResponse> getCustomerById(@PathVariable("id") String id);

}
