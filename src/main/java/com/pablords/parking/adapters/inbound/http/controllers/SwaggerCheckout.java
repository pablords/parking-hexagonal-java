package com.pablords.parking.adapters.inbound.http.controllers;

import org.springframework.web.bind.annotation.RequestBody;

import com.pablords.parking.adapters.inbound.http.Constants;
import com.pablords.parking.adapters.inbound.http.dtos.CheckoutRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
import com.pablords.parking.adapters.inbound.http.handlers.ApiError;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = Constants.CHECKOUT_API_TAG)
public interface SwaggerCheckout {

    @Operation(summary = "Create a checkout", tags = Constants.CHECKOUT_API_TAG, method = "POST", operationId = "create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constants.CREATED, description = "Requisição válida", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CheckoutResponseDTO.class), examples = @ExampleObject(value = "{\"checkin\":{\"id\":\"549611f8-e416-4e3f-84c3-7bea0754f8f0\",\"slot\":{\"id\":1,\"occupied\":false},\"checkInTime\":\"2025-02-02T09:32:00\"},\"checkOutTime\":\"2025-02-02T09:32:00\",\"parkingFee\":0}")) }),
            @ApiResponse(responseCode = Constants.NOT_FOUND, description = "Requisição inválida", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(value = "{\"status\":404,\"error\":\"NotFound\",\"message\":\"No check-in found for plate: KEZ3670\",\"path\":\"/api/v1/checkouts\",\"timestamp\":\"2025-02-02T09:32:58\"}")) }),
            @ApiResponse(responseCode = Constants.UNPROCESSABLE_ENTITY, description = "Erros de validação", content = {
                    @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"plate\":\"Plate cannot be empty\"}")) }),
    })
    CheckoutResponseDTO checkOut(@RequestBody @Valid CheckoutRequestDTO request);
}
