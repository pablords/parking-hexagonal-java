package com.pablords.parking.adapters.outbound.repository;

import org.springframework.stereotype.Component;

import com.pablords.parking.core.models.Car;
import com.pablords.parking.core.ports.outbound.repository.CarRepositoryPort;
import com.pablords.parking.adapters.outbound.db.CarJpaDbAdapter;
import com.pablords.parking.adapters.outbound.models.CarAdapter;

@Component
public class CarRepositoryAdapter implements CarRepositoryPort {

    private final CarJpaDbAdapter carJpaDbAdapter;

    public CarRepositoryAdapter(CarJpaDbAdapter carJpaDbAdapter) {
        this.carJpaDbAdapter = carJpaDbAdapter;
    }

    @Override
    public void create(Car car) {
        var carAdapter = new CarAdapter(car);
        this.carJpaDbAdapter.save(carAdapter);
    }

}
