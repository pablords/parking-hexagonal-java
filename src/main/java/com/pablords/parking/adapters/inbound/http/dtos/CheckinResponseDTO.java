package com.pablords.parking.adapters.inbound.http.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pablords.parking.core.entities.Slot;

import lombok.Data;

@Data
public class CheckinResponseDTO {
    private UUID id;
    private Slot slot;
    private LocalDateTime checkInTime;
}
