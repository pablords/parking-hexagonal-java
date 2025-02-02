package com.pablords.parking.adapters.inbound.http.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.pablords.parking.adapters.inbound.http.Constants;
import com.pablords.parking.adapters.inbound.http.dtos.CarRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;
import com.pablords.parking.adapters.inbound.http.handlers.ApiError;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = Constants.CAR_API_TAG)
public interface SwaggerCar {


        @Operation(summary = "Create a car", tags = Constants.CAR_API_TAG, method = "POST", operationId = "create")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = Constants.CREATED, description = "Requisição válida", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDTO.class), examples = @ExampleObject(value = "{\"id\":\"d47674ef-2219-423e-885d-d1f4ed125d55\",\"plate\":\"MWG1752\",\"brand\":\"Matra\",\"color\":\"branco\",\"model\":\"Pick-Up4x2Curto/Longo2.5TDIDiesel\"}")) }),
                        @ApiResponse(responseCode = Constants.CONFLICT, description = "Requisição inválida", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(value = "{\"status\":409,\"error\":\"Conflict\",\"message\":\"A car with is plate already exists: MWG1752\",\"path\":\"/api/v1/cars\",\"timestamp\":\"2025-02-01T16:57:06\"}")) }),
                        @ApiResponse(responseCode = Constants.UNPROCESSABLE_ENTITY, description = "Erros de validação", content = {
                                        @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"plate\":\"Plate cannot be empty\"}")) }),
        })
        CarResponseDTO create(@RequestBody @Valid CarRequestDTO request);

        @Operation(summary = "Find cars", tags = Constants.CAR_API_TAG, method = "GET", operationId = "find")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = Constants.OK, description = "Requisição válida", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CarResponseDTO.class)), examples = @ExampleObject(value = "[{\"id\":\"79de3fe8-0b2e-445d-aee9-deafd30526ae\",\"plate\":\"KEZ3670\",\"brand\":\"Seat\",\"color\":\"Preto\",\"model\":\"CordobaVario1.6L\"}]")) }),
                        @ApiResponse(responseCode = Constants.NOT_FOUND, description = "Carros não encontrados", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) })

        })
        List<CarResponseDTO> find();

        @Operation(summary = "Find a car by plate", tags = Constants.CAR_API_TAG)
        @ApiResponses(value = {
                        @ApiResponse(responseCode = Constants.OK, description = "Requisição válida", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDTO.class), examples = @ExampleObject(value = "{\"id\":\"d47674ef-2219-423e-885d-d1f4ed125d55\",\"plate\":\"MWG1752\",\"brand\":\"Matra\",\"color\":\"branco\",\"model\":\"Pick-Up4x2Curto/Longo2.5TDIDiesel\"}")) }),
                        @ApiResponse(responseCode = Constants.NOT_FOUND, description = "Carros não encontrados", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) })

        })
        CarResponseDTO findByPlate(@PathVariable String plate);

}
