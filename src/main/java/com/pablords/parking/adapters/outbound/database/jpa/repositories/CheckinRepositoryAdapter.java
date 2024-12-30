package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.CheckinMapper;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class CheckinRepositoryAdapter implements CheckinRepositoryPort {

    private final JpaRepositoryCheckin jpaRepositoryCheckin;

    public CheckinRepositoryAdapter(JpaRepositoryCheckin jpaRepositoryCheckin) {
        this.jpaRepositoryCheckin = jpaRepositoryCheckin;
    }

    @Override
    public Checkin save(Checkin checkin) {
        var checkinModel = jpaRepositoryCheckin.save(CheckinMapper.toModel(checkin));
        checkin.setId(checkinModel.getId());
        return checkin;
    }

    @Override
    public Optional<Checkin> findByPlate(String plate) {
        plate = plate.trim().toUpperCase();
        var checkinModel = jpaRepositoryCheckin.findLatestByCarPlate(plate);
        return Optional.of(CheckinMapper.toEntity(checkinModel.get()));
    }

    @Override
    public Optional<Checkin> findById(Long id) {
        var checkinModel = jpaRepositoryCheckin.findById(id);
        return Optional.of(CheckinMapper.toEntity(checkinModel.get()));
    }

}
