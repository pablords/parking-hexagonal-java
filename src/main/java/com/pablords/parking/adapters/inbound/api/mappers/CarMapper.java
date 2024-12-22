package com.pablords.parking.adapters.inbound.api.mappers;

import com.pablords.parking.adapters.inbound.api.dtos.CreateCarDTO;
import com.pablords.parking.core.entities.Car;

public class CarMapper {
    private CarMapper() {
        // private constructor to hide the implicit public one
    }
    public static Car toEntity(CreateCarDTO createCarDto) {
        Car car = new Car();
        car.setPlate(createCarDto.getPlate());
        car.setBrand(createCarDto.getBrand());
        return car;
    }
}
