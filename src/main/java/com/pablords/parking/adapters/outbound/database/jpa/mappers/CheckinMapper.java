package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.core.entities.Checkin;

@Mapper(componentModel = "spring", uses = {CarMapper.class, SlotMapper.class})
public interface CheckinMapper {

  @Mapping(target = "car", source = "car")
  @Mapping(target = "slot", source = "slot")
  CheckinModel toModel(Checkin checkin);

  @Mapping(target = "car", source = "car")
  @Mapping(target = "slot", source = "slot")
  Checkin toEntity(CheckinModel model);
}