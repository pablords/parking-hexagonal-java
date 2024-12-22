package com.pablords.parking.core.ports.inbound.services;

import com.pablords.parking.adapters.inbound.api.dtos.CreateCarDTO;
import com.pablords.parking.core.entities.Car;

public interface CarServicePort {
    Car create(CreateCarDTO createCarDto); 
}
