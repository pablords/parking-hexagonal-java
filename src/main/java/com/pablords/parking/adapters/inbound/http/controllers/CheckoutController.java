package com.pablords.parking.adapters.inbound.http.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
import com.pablords.parking.core.ports.inbound.services.CheckoutServicePort;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkouts")
public class CheckoutController {

    private final ModelMapper modelMapper = new ModelMapper();

    private final CheckoutServicePort checkoutServicePort;

    public CheckoutController(CheckoutServicePort checkoutServicePort) {
        this.checkoutServicePort = checkoutServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CheckoutResponseDTO checkOut(@RequestBody @Valid String plate) {
        var response = checkoutServicePort.checkout(plate);
        return modelMapper.map(response, CheckoutResponseDTO.class);
    }
}
