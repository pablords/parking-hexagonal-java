package com.pablords.parking.core.ports.outbound.repository;

import com.pablords.parking.core.models.Car;

public interface CarRepositoryPort {
    void create(Car car);
}
