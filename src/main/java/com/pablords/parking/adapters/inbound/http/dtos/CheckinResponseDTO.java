package com.pablords.parking.adapters.inbound.http.dtos;

import java.time.LocalDateTime;

import com.pablords.parking.core.entities.Slot;

import lombok.Data;

@Data
public class CheckinResponseDTO {
    private Long id;
    private Slot slot;
    private LocalDateTime checkInTime;
}
