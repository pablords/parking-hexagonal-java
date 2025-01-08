package com.pablords.parking.adapters.inbound.http.mappers;

import com.pablords.parking.adapters.inbound.http.dtos.CheckinRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.valueobjects.Plate;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
@Slf4j
public class CheckinMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static Car toEntity(CheckinRequestDTO checkinRequestDTO) {
        log.debug("Mapeando CheckinRequestDTO para Car: {}", checkinRequestDTO);
        modelMapper.typeMap(CheckinRequestDTO.class, Car.class)
                .addMappings(mapper -> mapper.skip(Car::setPlate)) // Skip default mapping for Plate
                .setPostConverter(context -> {
                    var source = context.getSource();
                    var destination = context.getDestination();
                    destination.setPlate(new Plate(source.getPlate()));
                    return destination;
                });
        return modelMapper.map(checkinRequestDTO, Car.class);
    }

    public static CheckinResponseDTO toResponse(Checkin checkin) {
        log.debug("Mapeando Checkin para CheckinResponseDTO: {}", checkin);
        return modelMapper.map(checkin, CheckinResponseDTO.class);
    }
}
