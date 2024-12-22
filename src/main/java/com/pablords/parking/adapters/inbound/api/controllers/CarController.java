package com.pablords.parking.adapters.inbound.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.parking.adapters.inbound.api.dtos.CreateCarDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarServicePort carServiceAdapter;

    public CarController(CarServicePort carServicePort) {
        this.carServiceAdapter = carServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody @Valid CreateCarDTO createCarDto) {
        this.carServiceAdapter.create(createCarDto);
        return ResponseEntity.ok().build();
    }
}
