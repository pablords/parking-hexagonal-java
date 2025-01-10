package com.pablords.parking.core.services;

import java.util.List;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;

public class CarService implements CarServicePort {
    private final CarRepositoryPort carRepositoryPort;

    public CarService(CarRepositoryPort carRepositoryPort) {
        this.carRepositoryPort = carRepositoryPort;
    }

    @Override
    public Car create(Car car) {

        Boolean plateExists = carRepositoryPort.existsByPlate(car.getPlate().getValue());
        if (plateExists) {
            throw new ExistPlateException(
                    String.format(ErrorMessages.CAR_WITH_PLATE_EXISTS, car.getPlate().getValue()));
        }
        return carRepositoryPort.save(car);
    }

    @Override
    public List<Car> find() {
        return carRepositoryPort.find();
    }

    @Override  
    public Car findByPlate(String plate) {
        return carRepositoryPort.findByPlate(plate)
                .orElseThrow(() -> new ExistPlateException(String.format(ErrorMessages.CAR_WITH_PLATE_EXISTS, plate)));
    }

}
