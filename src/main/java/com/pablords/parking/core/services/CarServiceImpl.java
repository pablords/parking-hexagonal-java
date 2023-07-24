package com.pablords.parking.core.services;

import com.pablords.parking.core.models.Car;
import com.pablords.parking.core.models.dtos.CreateCarDto;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import com.pablords.parking.core.ports.outbound.repository.CarRepositoryPort;

public class CarServiceImpl implements CarServicePort {
    private final CarRepositoryPort carRepositoryPort;

    public CarServiceImpl(CarRepositoryPort carRepositoryPort) {
        this.carRepositoryPort = carRepositoryPort;
    }

    @Override
    public void create(CreateCarDto createCarDto) {
        var car = new Car();
        car.setBrand(createCarDto.getBrand());
        car.setPlate(createCarDto.getPlate());
        this.carRepositoryPort.create(car);
    }

}
