package com.pablords.parking.adapters.inbound.http.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class CarController implements SwaggerCar {

  private final CarServicePort carServicePort;

  public CarController(CarServicePort carServicePort) {
    this.carServicePort = carServicePort;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CarResponseDTO create(@RequestBody @Valid CarRequestDTO createCarDto) {
    log.info("Recebendo requisição para criação de carro com payload={}", createCarDto.toString());
    var car = CarMapper.toEntity(createCarDto);
    var createdCar = this.carServicePort.create(car);
    log.info("Respondendo com status={} para carro com response={}", HttpStatus.CREATED, createdCar.toString());
    return CarMapper.toDTO(createdCar);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<CarResponseDTO> find() {
    var cars = this.carServicePort.find();
    return cars.stream()
        .map(CarMapper::toDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/{plate}")
  @ResponseStatus(HttpStatus.OK)
  public CarResponseDTO findByPlate(@PathVariable String plate) {
    var car = this.carServicePort.findByPlate(plate);
    return CarMapper.toDTO(car);
  }
}
