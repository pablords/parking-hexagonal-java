package com.pablords.parking.adapters.outbound.mappers;

import com.pablords.parking.adapters.outbound.models.CarModel;
import com.pablords.parking.core.entities.Car;

public class CarMapper {
    public static CarModel toModel(Car car) {
        CarModel model = new CarModel();
        model.setId(car.getId());
        model.setBrand(car.getBrand());
        model.setPlate(car.getPlate().getValue());
        return model;
    }

    public static Car toEntity(CarModel model) {
        Car car = new Car();
        car.setId(model.getId());
        car.setBrand(model.getBrand());
        return car;
    }
}
