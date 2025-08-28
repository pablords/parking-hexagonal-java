package com.pablords.parking.adapters.inbound.http.dtos;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CarResponseDTO(
    @JsonProperty UUID id,
    @JsonProperty String plate,
    @JsonProperty String brand,
    @JsonProperty String color,
    @JsonProperty String model
) {}



