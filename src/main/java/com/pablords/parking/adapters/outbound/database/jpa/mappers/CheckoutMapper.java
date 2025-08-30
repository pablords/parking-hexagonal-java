package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import java.time.LocalDateTime;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.core.entities.Checkout;

@Mapper(componentModel = "spring", uses = { CheckinMapper.class, CarMapper.class, SlotMapper.class })

public interface CheckoutMapper {
  CheckoutModel toModel(Checkout checkout);

  Checkout toEntity(CheckoutModel model);

  @AfterMapping
  default void calculateFee(@MappingTarget Checkout checkout) {
    if (checkout.getCheckOutTime() == null) {
      checkout.setCheckOutTime(LocalDateTime.now());
    }
    if (checkout.getCheckin() != null && checkout.getCheckOutTime() != null) {
      checkout.updateParkingFee();
    }
  }
}
