package com.pablords.parking.adapters.inbound.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckinRequestDTO {
    @NotBlank(message = "Plate cannot be empty")
    @JsonProperty
    String plate;
    @NotBlank(message = "Brand cannot be empty")
    @JsonProperty
    String brand;
    @NotBlank(message = "Color cannot be empty")
    @JsonProperty
    String color;
    @NotBlank(message = "Model cannot be empty")
    @JsonProperty
    String model;
}
