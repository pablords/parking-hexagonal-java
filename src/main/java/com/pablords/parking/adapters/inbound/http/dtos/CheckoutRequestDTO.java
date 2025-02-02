package com.pablords.parking.adapters.inbound.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutRequestDTO {
    @JsonProperty
    @NotBlank(message = "Plate cannot be empty")
    private String plate;
}
