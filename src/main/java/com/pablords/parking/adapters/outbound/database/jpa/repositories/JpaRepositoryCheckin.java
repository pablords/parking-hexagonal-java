package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.core.entities.Checkin;

@Repository
public interface JpaRepositoryCheckin extends JpaRepository<CheckinModel, Long> {
    Optional<Checkin> findByCarId(Long carId);
}
