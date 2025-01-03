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
        return jpaRepositoryCheckin.findLatestByCarPlate(plate)
                .map(CheckinMapper::toEntity);
    }

    @Override
    public Optional<Checkin> findById(Long id) {
        return jpaRepositoryCheckin.findById(id).map(CheckinMapper::toEntity);
    }

}
