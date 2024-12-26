package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;



@Repository
public interface JpaRepositoryCar extends JpaRepository<CarModel, Long> {
    boolean existsByPlate(String plate);
}
