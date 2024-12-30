package com.pablords.parking.adapters.inbound.http.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckinRequestDTO {
    @NotBlank
    String plate;
    @NotBlank
    String brand;
    @NotBlank
    String color;
    @NotBlank
    String model;
}
