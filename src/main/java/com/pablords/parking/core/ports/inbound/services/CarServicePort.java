package com.pablords.parking.core.ports.inbound.services;

import com.pablords.parking.core.models.dtos.CreateCarDto;

public interface CarServicePort {
    void create(CreateCarDto createCarDto); 
}
