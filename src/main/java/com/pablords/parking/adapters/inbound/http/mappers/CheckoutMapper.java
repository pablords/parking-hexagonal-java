package com.pablords.parking.adapters.inbound.http.mappers;

import org.modelmapper.ModelMapper;

import com.pablords.parking.adapters.inbound.http.dtos.CheckoutRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
import com.pablords.parking.core.entities.Checkout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  public static Checkout toEntity(CheckoutRequestDTO checkoutRequestDTO) {
    log.info("Mapeando CheckoutRequestDTO para Checkout: {}", checkoutRequestDTO);
    return modelMapper.map(checkoutRequestDTO, Checkout.class);
  }

  public static CheckoutResponseDTO toDTO(Checkout checkout) {
    log.info("Mapeando Checkout para CheckoutResponseDTO: {}", checkout);
    modelMapper.typeMap(Checkout.class, CheckoutResponseDTO.class)
        .setPostConverter(context -> {
          Checkout checkoutEntity = context.getSource();
          CheckoutResponseDTO checkoutResponseDTO = context.getDestination();
          checkoutResponseDTO.setCheckOutTime(checkoutEntity.getCheckin().getCheckOutTime());
          return checkoutResponseDTO;
        });
    return modelMapper.map(checkout, CheckoutResponseDTO.class);
  }
}
