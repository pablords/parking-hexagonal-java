package com.pablords.parking.adapters.inbound.api.dtos.req;

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

}
