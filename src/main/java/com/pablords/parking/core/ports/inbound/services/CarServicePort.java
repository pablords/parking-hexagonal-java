package com.pablords.parking.core.ports.inbound.services;

import java.util.List;


import com.pablords.parking.core.entities.Car;

public interface CarServicePort {
    Car create(Car car); 
    List<Car> find();
    Car findByPlate(String plate);
}
