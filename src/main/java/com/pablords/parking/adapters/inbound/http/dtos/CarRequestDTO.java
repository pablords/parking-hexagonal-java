package com.pablords.parking.adapters.inbound.http.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CarRequestDTO {
    @NotBlank
    String plate;
    @NotBlank
    String brand;
    @NotBlank
    String color;
    @NotBlank
    String model;
}
