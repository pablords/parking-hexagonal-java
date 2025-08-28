package com.pablords.parking.adapters.inbound.http.mappers;

import com.pablords.parking.adapters.inbound.http.dtos.CarRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.valueobjects.Plate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CarMapper {

    public static Car toEntity(CarRequestDTO dto) {
        log.info("Mapeando CarRequestDTO para Car: {}", dto);
        return new Car(
            new Plate(dto.plate()),
            dto.brand(),
            dto.color(),
            dto.model()
        );
    }

    public static CarResponseDTO toDTO(Car car) {
        log.info("Mapeando Car para CarResponseDTO: {}", car);
        return new CarResponseDTO(
            car.getId(),
            car.getPlate() != null ? car.getPlate().getValue() : null,
            car.getBrand(),
            car.getColor(),
            car.getModel()
        );
    }
}
