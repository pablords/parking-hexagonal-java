package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import org.mapstruct.Mapper;

import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.core.entities.Slot;

@Mapper(componentModel = "spring")
public interface SlotMapper {

  SlotModel toModel(Slot slot);

  Slot toEntity(SlotModel model);

}
