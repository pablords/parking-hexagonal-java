package com.pablords.parking.adapters.inbound.http.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CheckoutResponseDTO {
    private CheckinResponseDTO checkin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOutTime;
    private Double parkingFee;
}
