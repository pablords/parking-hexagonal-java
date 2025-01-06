package com.pablords.parking.adapters.inbound.http.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class CarResponseDTO {
    private UUID id;
    private String plate;
    private String brand;
    private String color;
    private String model;
}



