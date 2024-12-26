package com.pablords.parking.core.services;

import java.util.List;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.exceptions.InvalidPlateException;
import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.utils.StringUtils;

public class CarServiceImpl implements CarServicePort {
    private final CarRepositoryPort carRepositoryPort;

    public CarServiceImpl(CarRepositoryPort carRepositoryPort) {
        this.carRepositoryPort = carRepositoryPort;
    }

    @Override
    public Car create(Car car) {
        boolean plateExists = carRepositoryPort.find()
                .stream()
                .anyMatch(existingCar -> StringUtils.equalsIgnoreCase(existingCar.getPlate().getValue(),
                        car.getPlate().getValue()));

        if (plateExists) {
            throw new ExistPlateException("A car with this plate already exists: " + car.getPlate().getValue());
        }

        return carRepositoryPort.create(car);
    }

    @Override
    public List<Car> find() {
        return carRepositoryPort.find();
    }

}
