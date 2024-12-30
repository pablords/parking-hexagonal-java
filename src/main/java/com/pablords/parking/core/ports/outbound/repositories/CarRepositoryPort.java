package com.pablords.parking.core.ports.outbound.repositories;

import java.util.List;
import java.util.Optional;

import com.pablords.parking.core.entities.Car;

public interface CarRepositoryPort {
    boolean existsByPlate(String plate);
    Optional<Car> findByPlate(String plate);
    Optional<Car> findById(Long id);
    Car save(Car car);
    List<Car> find();
}
