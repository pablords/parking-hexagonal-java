package com.pablords.parking.adapters.inbound.http.mappers;

import com.pablords.parking.adapters.inbound.http.dtos.CheckinRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.valueobjects.Plate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckinMapper {

    public static Car toEntity(CheckinRequestDTO dto) {
        log.info("Mapeando CheckinRequestDTO para Car: {}", dto);
        return new Car(
            new Plate(dto.plate()),
            dto.brand(),
            dto.color(),
            dto.model()
        );
    }

    public static CheckinResponseDTO toDTO(Checkin checkin) {
        log.info("Mapeando Checkin para CheckinResponseDTO: {}", checkin);
        return new CheckinResponseDTO(
            checkin.getId(),
            checkin.getSlot(),
            checkin.getCheckInTime()
        );
    }
}
