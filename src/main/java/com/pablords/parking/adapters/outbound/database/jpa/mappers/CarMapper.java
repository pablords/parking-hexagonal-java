package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import org.modelmapper.ModelMapper;

import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.valueobjects.Plate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CarMapper {

    private static final ModelMapper modelMapper = new ModelMapper();
    
    public static CarModel toModel(Car car) {
        log.info("Mapeando Car para CarModel {}", car.toString());
        modelMapper.typeMap(Car.class, CarModel.class)
                .addMappings(mapper -> mapper.skip(CarModel::setPlate)) // Skip default mapping for Plate
                .setPostConverter(context -> {
                    context.getDestination().setPlate(context.getSource().getPlate().getValue());
                    return context.getDestination();
                });
        return modelMapper.map(car, CarModel.class);
    }

    public static Car toEntity(CarModel model) {
        log.info("Mapeando CarModel para Car {}", model.toString());
        modelMapper.typeMap(CarModel.class, Car.class)
                .addMappings(mapper -> mapper.skip(Car::setPlate)) // Skip default mapping for Plate
                .setPostConverter(context -> {
                    context.getDestination().setPlate(new Plate(context.getSource().getPlate()));
                    return context.getDestination();
                });
        return modelMapper.map(model, Car.class);
    }
}
