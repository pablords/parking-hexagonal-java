package com.pablords.parking.core.services;

import java.util.List;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import com.pablords.parking.core.ports.outbound.repository.CarRepositoryPort;

public class CarServiceImpl implements CarServicePort {
    private final CarRepositoryPort carRepositoryPort;

    public CarServiceImpl(CarRepositoryPort carRepositoryPort) {
        this.carRepositoryPort = carRepositoryPort;
    }

    @Override
    public Car create(Car car) {
        return this.carRepositoryPort.create(car);
    }

    @Override
    public List<Car> find() {
        return this.carRepositoryPort.find();
    }

}
