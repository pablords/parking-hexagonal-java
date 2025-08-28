package com.pablords.parking.adapters.inbound.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CheckoutRequestDTO(
    @NotBlank(message = "Plate cannot be empty") @JsonProperty String plate
) {}
