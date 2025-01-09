package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;


@Repository
public interface JpaCarRepository extends JpaRepository<CarModel, UUID> {
    boolean existsByPlate(String plate);
    Optional<CarModel> findByPlate(String plate);
}
