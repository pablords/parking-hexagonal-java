package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.adapters.outbound.database.jpa.mappers.CarMapper;
import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;

@Component
public class CarRepositoryAdapter implements CarRepositoryPort {

    private final JpaRepositoryCar jpaRepositoryCar;

    public CarRepositoryAdapter(JpaRepositoryCar jpaRepositoryCar) {
        this.jpaRepositoryCar = jpaRepositoryCar;
    }

    @Override
    public Optional<Car> findById(UUID id) {
        return jpaRepositoryCar.findById(id).map(CarMapper::toEntity);
    }

    @Override
    public List<Car> find() {
        List<CarModel> carsModel = jpaRepositoryCar.findAll();
        return carsModel.stream()
                .map(car -> CarMapper.toEntity(car))
                .collect(Collectors.toList());
    }

    @Override
    public Car save(Car car) {
        CarModel carModel = jpaRepositoryCar.save(CarMapper.toModel(car));
        return CarMapper.toEntity(carModel);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return jpaRepositoryCar.existsByPlate(plate);
    }

    @Override
    public Optional<Car> findByPlate(String plate) {
        var carModel = jpaRepositoryCar.findByPlate(plate);
        return Optional.of(CarMapper.toEntity(carModel.get()));
    }

}
