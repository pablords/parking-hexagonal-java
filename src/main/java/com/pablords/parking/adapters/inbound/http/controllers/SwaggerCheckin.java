package com.pablords.parking.adapters.inbound.http.controllers;

import org.springframework.web.bind.annotation.RequestBody;

import com.pablords.parking.adapters.inbound.http.Constants;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.adapters.inbound.http.handlers.ApiErrorDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = Constants.CHECKIN_API_TAG)
public interface SwaggerCheckin {
    @Operation(summary = "Create a checkin", tags = Constants.CHECKIN_API_TAG, method = "POST", operationId = "create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constants.CREATED, description = "Requisição válida", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CheckinResponseDTO.class), examples = @ExampleObject(value = "{\"id\":\"83dafdf2-fa18-4077-b658-927f24032b85\",\"slot\":{\"id\":1,\"occupied\":true},\"checkInTime\":\"2025-02-02T09:54:51\"}")) }),
            @ApiResponse(responseCode = Constants.BAD_REQUEST, description = "Requisição inválida", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDTO.class), examples = @ExampleObject(value = "{\"status\":400,\"error\":\"BadRequest\",\"message\":\"Invalid checkin,checkout notfound\",\"path\":\"/api/v1/checkins\",\"timestamp\":\"2025-02-02T09:55:23\"}")) }),
            @ApiResponse(responseCode = Constants.UNPROCESSABLE_ENTITY, description = "Erros de validação", content = {
                    @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"plate\":\"Plate cannot be empty\"}")) }),
    })
    CheckinResponseDTO checkin(@RequestBody @Valid CheckinRequestDTO request);
}
