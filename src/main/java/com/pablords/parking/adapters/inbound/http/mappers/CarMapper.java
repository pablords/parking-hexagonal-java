package com.pablords.parking.adapters.inbound.http.mappers;

import javax.print.attribute.standard.Destination;

import org.modelmapper.ModelMapper;

import com.pablords.parking.adapters.inbound.http.dtos.CarRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.valueObjects.Plate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CarMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static Car toEntity(CarRequestDTO createCarDTO) {
        log.debug("CarRequestDTO: {}", createCarDTO);
        modelMapper.typeMap(CarRequestDTO.class, Car.class)
                .addMappings(mapper -> mapper.skip(Car::setPlate)) // Skip default mapping for Plate
                .setPostConverter(context -> {
                    var source = context.getSource();
                    var destination = context.getDestination();
                    destination.setPlate(new Plate(source.getPlate()));
                    return destination;
                });
        return modelMapper.map(createCarDTO, Car.class);
    }

    public static CarResponseDTO toResponse(Car car) {
        log.debug("Car: {}", car);
        modelMapper.typeMap(Car.class, CarResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getPlate().getValue(),
                    CarResponseDTO::setPlate);
        });
        return modelMapper.map(car, CarResponseDTO.class);
    }
}
