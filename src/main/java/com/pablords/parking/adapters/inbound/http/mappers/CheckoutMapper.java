package com.pablords.parking.adapters.inbound.http.mappers;

import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
import com.pablords.parking.core.entities.Checkout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutMapper {


  public static CheckoutResponseDTO toDTO(Checkout checkout) {
    log.info("Mapeando Checkout para CheckoutResponseDTO: {}", checkout);
    return new CheckoutResponseDTO(
      CheckinMapper.toDTO(checkout.getCheckin()),
      checkout.getCheckOutTime(),
      checkout.getParkingFee()
    );
  }
}
