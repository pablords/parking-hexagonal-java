package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.CheckinMapper;
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
    private final CheckinMapper checkinMapper;

    public CheckinRepositoryAdapter(JpaCheckinRepository jpaRepositoryCheckin, CheckinMapper checkinMapper) {
        this.jpaRepositoryCheckin = jpaRepositoryCheckin;
        this.checkinMapper = checkinMapper;
    }

    @Override
    public Checkin save(Checkin checkin) {
        log.info("Persistindo checkin: {} no slot: {}", checkin.toString(), checkin.getSlot().toString());
        var checkinModel = jpaRepositoryCheckin.save(checkinMapper.toModel(checkin));
        return CheckinMapper.toEntity(checkinModel);
    }

    @Override
    public Optional<Checkin> findByPlate(String plate) {
        log.info("Buscando checkin de carro com Placa: {}", plate);
        plate = plate.trim().toUpperCase();
        return jpaRepositoryCheckin.findLatestByCarPlate(plate)
                .map(CheckinMapper::toEntity);
    }

    @Override
    public Optional<Checkin> findById(UUID id) {
        return jpaRepositoryCheckin.findById(id).map(CheckinMapper::toEntity);
    }

}
