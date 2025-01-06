package com.pablords.parking.adapters.inbound.http.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CarResponseDTO {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private String plate;
    @JsonProperty
    private String brand;
    @JsonProperty
    private String color;
    @JsonProperty
    private String model;
}



