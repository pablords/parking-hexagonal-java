package com.pablords.parking.adapters.inbound.api.mappers;

import com.pablords.parking.adapters.inbound.api.dtos.req.CarRequestDTO;
import com.pablords.parking.adapters.inbound.api.dtos.res.CarResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.valueObjects.Plate;

public class CarMapper {
    private CarMapper() {
        // private constructor to hide the implicit public one
    }

    public static Car toEntity(CarRequestDTO createCarDTO) {
        Car car = new Car();
        var plate = new Plate(createCarDTO.getPlate());
        car.setPlate(plate);
        car.setBrand(createCarDTO.getBrand());
        car.setColor(createCarDTO.getColor());
        return car;
    }

    public static CarResponseDTO toResponse(Car car) {
        var carResponse = new CarResponseDTO();
        carResponse.setId(car.getId());
        carResponse.setPlate(car.getPlate().getValue());
        carResponse.setBrand(car.getBrand());
        carResponse.setColor(car.getColor());
        return carResponse;
    }
}
