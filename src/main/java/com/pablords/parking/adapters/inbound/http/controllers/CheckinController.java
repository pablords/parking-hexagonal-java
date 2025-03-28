package com.pablords.parking.adapters.inbound.http.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.parking.adapters.inbound.http.dtos.CheckinRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.adapters.inbound.http.mappers.CheckinMapper;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/checkins")
@Slf4j
public class CheckinController implements SwaggerCheckin {

    private final CheckinServicePort checkinServicePort;

    public CheckinController(CheckinServicePort checkinServicePort) {
        this.checkinServicePort = checkinServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CheckinResponseDTO checkin(@RequestBody @Valid CheckinRequestDTO checkinRequestDTO) {
        log.info("Recebendo requisição para estacionamento do carro com a placa: {}, payload={}",
        checkinRequestDTO.getPlate(), checkinRequestDTO.toString());
        var checkin = CheckinMapper.toEntity(checkinRequestDTO);
        var persistedCheckin = checkinServicePort.checkIn(checkin);
        var checkinResponse = CheckinMapper.toDTO(persistedCheckin);
        log.info("Respondendo com status={} para carro com a placa: {} response={}", HttpStatus.CREATED,
        checkinRequestDTO.getPlate(), checkinResponse.toString());
        return checkinResponse;
    }
}
