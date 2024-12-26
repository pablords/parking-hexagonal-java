package com.pablords.parking.adapters.inbound.api.dtos.res;

import lombok.Data;

@Data
public class CarResponseDTO {
    private Long id;
    private String plate;
    private String brand;
    private String color;
    private String model;
}



