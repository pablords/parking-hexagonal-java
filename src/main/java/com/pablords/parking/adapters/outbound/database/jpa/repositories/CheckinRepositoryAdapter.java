package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.CarMapper;
import com.pablords.parking.adapters.outbound.database.jpa.mappers.CheckinMapper;
import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckinRepositoryAdapter implements CheckinRepositoryPort {

  private final JpaCheckinRepository jpaRepositoryCheckin;
  private final JpaCarRepository jpaRepositoryCar;
  private final CheckinMapper checkinMapper;
  private final CarMapper carMapper;

  public CheckinRepositoryAdapter(JpaCheckinRepository jpaRepositoryCheckin, JpaCarRepository jpaRepositoryCar,
      CheckinMapper checkinMapper, CarMapper carMapper) {
    this.jpaRepositoryCheckin = jpaRepositoryCheckin;
    this.jpaRepositoryCar = jpaRepositoryCar;
    this.checkinMapper = checkinMapper;
    this.carMapper = carMapper;
  }

  @Override
  public Checkin save(Checkin checkin) {
    log.info("Persistindo checkin: {} no slot: {}", checkin.toString(), checkin.getSlot().toString());

    // Recupera o modelo JPA do carro para evitar instâncias transientes
    CarModel carModel = jpaRepositoryCar.findByPlate(checkin.getCar().getPlate().getValue())
        .orElse(null);
        
    checkin.setCar(carMapper.toEntity(carModel));

    var checkinModel = jpaRepositoryCheckin.save(checkinMapper.toModel(checkin));
    return checkinMapper.toEntity(checkinModel);
  }

  @Override
  public Optional<Checkin> findByPlate(String plate) {
    log.info("Buscando checkin de carro com Placa: {}", plate);
    plate = plate.trim().toUpperCase();
    return jpaRepositoryCheckin.findLatestByCarPlate(plate)
        .map(checkinMapper::toEntity);
  }

  @Override
  public Optional<Checkin> findById(UUID id) {
    return jpaRepositoryCheckin.findById(id).map(checkinMapper::toEntity);
  }

}
