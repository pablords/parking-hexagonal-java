package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.valueobjects.Plate;

@Mapper(componentModel = "spring")
public interface CarMapper {

  @Mapping(target = "plate", source = "plate", qualifiedByName = "plateToString")
  CarModel toModel(Car car);

  @Mapping(target = "plate", source = "plate", qualifiedByName = "stringToPlate")
  Car toEntity(CarModel model);

  @Named("plateToString")
  default String plateToString(Plate plate) {
    return plate != null ? plate.getValue() : null;
  }

  @Named("stringToPlate")
  default Plate stringToPlate(String plate) {
    return plate != null ? new Plate(plate) : null;
  }
}