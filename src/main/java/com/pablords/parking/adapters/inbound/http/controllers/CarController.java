package com.pablords.parking.adapters.inbound.http.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.parking.adapters.inbound.http.dtos.CarRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;
import com.pablords.parking.adapters.inbound.http.mappers.CarMapper;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cars")
@Slf4j
public class CarController {

    private final CarServicePort carServiceAdapter;

    public CarController(CarServicePort carServicePort) {
        this.carServiceAdapter = carServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponseDTO create(@RequestBody @Valid CarRequestDTO createCarDto) {
        var car = CarMapper.toEntity(createCarDto);
        var createdCar = this.carServiceAdapter.create(car);
        return CarMapper.toResponse(createdCar);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarResponseDTO> find() {
        var cars = this.carServiceAdapter.find();
        log.debug("CarController: cars = {}", cars);
        return cars.stream()
                .map(car -> CarMapper.toResponse(car))
                .collect(Collectors.toList());
    }
}
