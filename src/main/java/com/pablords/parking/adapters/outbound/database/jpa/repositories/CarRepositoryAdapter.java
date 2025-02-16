package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;

import lombok.extern.slf4j.Slf4j;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.CarMapper;
import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;

@Component
@Slf4j
public class CarRepositoryAdapter implements CarRepositoryPort {

    private final JpaCarRepository jpaRepositoryCar;

    public CarRepositoryAdapter(JpaCarRepository jpaRepositoryCar) {
        this.jpaRepositoryCar = jpaRepositoryCar;
    }

    @Override
    public Optional<Car> findById(UUID id) {
        log.info("Buscando carro com ID: {}", id);
        return jpaRepositoryCar.findById(id).map(CarMapper::toEntity);
    }

    @Override
    public List<Car> find() {
        List<CarModel> carsModel = jpaRepositoryCar.findAll();
        return carsModel.stream()
                .map(CarMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Car save(Car car) {
        log.info("Persistindo carro: {}", car.toString());
        CarModel carModel = CarMapper.toModel(car);

        if (!this.existsByPlate(car.getPlate().getValue())) {
            carModel = jpaRepositoryCar.save(carModel);
        }

        return CarMapper.toEntity(carModel);
    }

    @Override
    public boolean existsByPlate(String plate) {
        return jpaRepositoryCar.existsByPlate(plate);
    }

    @Override
    public Optional<Car> findByPlate(String plate) {
        return jpaRepositoryCar.findByPlate(plate).map(CarMapper::toEntity);
    }

}
