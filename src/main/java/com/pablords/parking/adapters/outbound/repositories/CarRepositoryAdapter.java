package com.pablords.parking.adapters.outbound.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.outbound.repository.CarRepositoryPort;
import com.pablords.parking.adapters.outbound.mappers.CarMapper;
import com.pablords.parking.adapters.outbound.models.CarModel;


@Component
public class CarRepositoryAdapter implements CarRepositoryPort {

    private final JpaRepositoryCar carJpaPort;

    public CarRepositoryAdapter(JpaRepositoryCar carJpaPort) {
        this.carJpaPort = carJpaPort;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return this.carJpaPort.findById(id).map(CarMapper::toEntity);
    }

    @Override
    public Car create(Car car) {
        CarModel carModel = this.carJpaPort.save(CarMapper.toModel(car));
        return CarMapper.toEntity(carModel);
    }

}
