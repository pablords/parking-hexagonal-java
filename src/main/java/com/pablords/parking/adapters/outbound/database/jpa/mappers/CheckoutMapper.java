package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import org.modelmapper.ModelMapper;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutMapper {
  private static final ModelMapper modelMapper = new ModelMapper();

  public static CheckoutModel toModel(Checkout checkout) {
    log.info("Mapeando Checkout para CheckoutModel: {}", checkout.toString());
    return modelMapper.map(checkout, CheckoutModel.class);
  }

  public static Checkout toEntity(CheckoutModel model, Checkin checkin) {
    log.info("Mapeando CheckoutModel para Checkout: {}", model.toString());
    Checkout checkout = modelMapper.map(model, Checkout.class);
    checkout.setCheckin(checkin);
    // Só calcula se ambos estiverem preenchidos
    if (checkout.getCheckin() != null && checkout.getCheckOutTime() != null) {
        checkout.updateParkingFee();
    } else {
        log.warn("Não foi possível calcular a taxa: checkin ou checkOutTime nulo. checkin={}, checkOutTime={}", checkout.getCheckin(), checkout.getCheckOutTime());
    }
    return checkout;
  }
}
