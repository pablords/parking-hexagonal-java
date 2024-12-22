package com.pablords.parking.core.ports.outbound.repository;

import java.util.Optional;

import com.pablords.parking.core.entities.Car;

public interface CarRepositoryPort {
    Optional<Car> findById(Long id);
    Car create(Car car);
}
