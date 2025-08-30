package com.pablords.parking.adapters.inbound.http.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pablords.parking.adapters.inbound.http.dtos.CheckoutRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
import com.pablords.parking.adapters.inbound.http.mappers.CheckoutMapper;
import com.pablords.parking.core.ports.inbound.services.CheckoutServicePort;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/checkouts")
@Slf4j
public class CheckoutController implements SwaggerCheckout {

    private final CheckoutServicePort checkoutServicePort;

    public CheckoutController(CheckoutServicePort checkoutServicePort) {
        this.checkoutServicePort = checkoutServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CheckoutResponseDTO checkOut(@RequestBody @Valid CheckoutRequestDTO checkoutRequestDTO) {
        log.info("Recebendo requisição para checkout do carro com a placa: {} o payload={}",
                checkoutRequestDTO.plate(), checkoutRequestDTO);
        var response = checkoutServicePort.checkout(checkoutRequestDTO.plate());
        var checkoutResponseDTO = CheckoutMapper.toDTO(response);
        log.info("Respondendo com status={} para carro com a placa: {} response={}", HttpStatus.CREATED,
                checkoutRequestDTO.plate(), checkoutResponseDTO.toString());
        return checkoutResponseDTO;
    }
}
