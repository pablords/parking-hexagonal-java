package com.pablords.parking.adapters.inbound.http.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pablords.parking.core.entities.Slot;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckinResponseDTO {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private Slot slot;
    @JsonProperty
    private LocalDateTime checkInTime;
}
