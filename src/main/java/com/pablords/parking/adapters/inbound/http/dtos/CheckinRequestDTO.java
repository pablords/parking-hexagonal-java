package com.pablords.parking.adapters.inbound.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckinRequestDTO {
    @NotBlank(message = "Plate cannot be empty")
    @JsonProperty
    String plate;
    @NotBlank
    @JsonProperty
    String brand;
    @NotBlank
    @JsonProperty
    String color;
    @NotBlank
    @JsonProperty
    String model;
}
