package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCarRepository;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.valueobjects.Plate;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckinMapper {

  private JpaCarRepository jRepositoryCar;
  private static final ModelMapper modelMapper = new ModelMapper();

  @Autowired
  public CheckinMapper(JpaCarRepository jRepositoryCar) {
    this.jRepositoryCar = jRepositoryCar;
  }

  public CheckinModel toModel(Checkin checkin) {
    log.info("Mapeando o Checkin para CheckinModel: {}", checkin.toString());
    var model = new CheckinModel();
    model.setId(checkin.getId());
    model.setSlot(SlotMapper.toModel(checkin.getSlot()));
    model.setCheckInTime(checkin.getCheckInTime());
    model.setCheckOutTime(checkin.getCheckOutTime());

    // Recupera o modelo JPA do carro para evitar instâncias transientes
    CarModel carModel = jRepositoryCar.findByPlate(checkin.getCar().getPlate().getValue())
        .orElse(null);
    model.setCar(carModel);

    if (carModel == null) {
      model.setCar(CarMapper.toModel(checkin.getCar()));
    }

    return model;
  }

  public static Checkin toEntity(CheckinModel checkinModel) {
    log.info("Mapeando o CheckinModel para Checkin: {}", checkinModel.toString());
    modelMapper.typeMap(CheckinModel.class, Checkin.class)
        .setPostConverter(context -> {
          context.getDestination().setCar(CarMapper.toEntity(checkinModel.getCar()));
          return context.getDestination();
        });
    return modelMapper.map(checkinModel, Checkin.class);
  }
}
