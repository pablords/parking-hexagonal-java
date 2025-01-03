package com.pablords.parking.core.ports.outbound.repositories;

import com.pablords.parking.core.entities.Checkin;

import java.util.Optional;

public interface CheckinRepositoryPort {
    Checkin save(Checkin checkin);
    Optional<Checkin> findByPlate(String plate);
    Optional<Checkin> findById(Long id);
}
