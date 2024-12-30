package com.pablords.parking.adapters.inbound.http.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pablords.parking.core.entities.Checkin;

import lombok.Data;

@Data
public class CheckoutResponseDTO {
    @JsonProperty
    private Checkin checkin;
    private LocalDateTime checkOutTime;
    private long parkingFee;
}
