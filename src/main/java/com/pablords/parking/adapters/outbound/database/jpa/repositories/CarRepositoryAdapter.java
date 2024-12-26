package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import java.util.List;
import java.util.Optional;
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
    public Optional<Car> findById(Long id) {
        return this.jpaRepositoryCar.findById(id).map(CarMapper::toEntity);
    }

    @Override
    public List<Car> find() {
        List<CarModel> carsModel = this.jpaRepositoryCar.findAll();
        return carsModel.stream()
                .map(car -> CarMapper.toEntity(car))
                .collect(Collectors.toList());
    }

    @Override
    public Car create(Car car) {
        CarModel carModel = this.jpaRepositoryCar.save(CarMapper.toModel(car));
        return CarMapper.toEntity(carModel);
    }

}
