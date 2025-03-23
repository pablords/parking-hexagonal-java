package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import org.modelmapper.ModelMapper;

import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.core.entities.Slot;

public class SlotMapper {

  private static final ModelMapper modelMapper = new ModelMapper();

  public static SlotModel toModel(Slot slot) {
    return modelMapper.map(slot, SlotModel.class);
  }

  public static Slot toEntity(SlotModel model) {
    return modelMapper.map(model, Slot.class);
  }
}
