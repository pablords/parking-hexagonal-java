package com.pablords.parking.adapters.inbound.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.parking.core.models.dtos.CreateCarDto;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;

@RestController
@RequestMapping("cars")
public class CarController {

    private final CarServicePort carServiceAdapter;

    public CarController(CarServicePort carServicePort) {
        this.carServiceAdapter = carServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateCarDto CreateCarDto) {
        this.carServiceAdapter.create(CreateCarDto);
    }
}
