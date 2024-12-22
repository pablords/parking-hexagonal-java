package com.pablords.parking.core.services;

import com.pablords.parking.adapters.inbound.api.dtos.CreateCarDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import com.pablords.parking.core.ports.outbound.repository.CarRepositoryPort;

public class CarServiceImpl implements CarServicePort {
    private final CarRepositoryPort carRepositoryPort;

    public CarServiceImpl(CarRepositoryPort carRepositoryPort) {
        this.carRepositoryPort = carRepositoryPort;
    }

    @Override
    public Car create(CreateCarDTO createCarDto) {
        var car = new Car();
        car.setBrand(createCarDto.getBrand());
        car.setPlate(createCarDto.getPlate());
        return this.carRepositoryPort.create(car);
  
    }

}
