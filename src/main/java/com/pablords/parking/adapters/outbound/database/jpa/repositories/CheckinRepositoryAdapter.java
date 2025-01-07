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

    private final JpaRepositoryCheckin jpaRepositoryCheckin;

    public CheckinRepositoryAdapter(JpaRepositoryCheckin jpaRepositoryCheckin) {
        this.jpaRepositoryCheckin = jpaRepositoryCheckin;
    }

    @Override
    public Checkin save(Checkin checkin) {
        var checkinModel = jpaRepositoryCheckin.save(CheckinMapper.toModel(checkin));
        return CheckinMapper.toEntity(checkinModel);
    }

    @Override
    public Optional<Checkin> findByPlate(String plate) {
        log.debug("Buscando checkin de carro com Placa: {}", plate);

        plate = plate.trim().toUpperCase();
        return jpaRepositoryCheckin.findLatestByCarPlate(plate)
                .map(CheckinMapper::toEntity);
    }

    @Override
    public Optional<Checkin> findById(UUID id) {
        return jpaRepositoryCheckin.findById(id).map(CheckinMapper::toEntity);
    }

}
