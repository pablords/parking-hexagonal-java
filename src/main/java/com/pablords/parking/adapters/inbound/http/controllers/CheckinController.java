package com.pablords.parking.adapters.inbound.http.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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

@RestController
@RequestMapping("/checkins")
public class CheckinController {

    private final CheckinServicePort chekinServicePort;

    public CheckinController(CheckinServicePort chekinServicePort) {
        this.chekinServicePort = chekinServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CheckinResponseDTO checkIn(@RequestBody @Valid CheckinRequestDTO chekinRequestDTO) {
        var chekin = CheckinMapper.toEntity(chekinRequestDTO);
        var chekinResponse = chekinServicePort.checkIn(chekin);
        return CheckinMapper.toResponse(chekinResponse);
    }
}
