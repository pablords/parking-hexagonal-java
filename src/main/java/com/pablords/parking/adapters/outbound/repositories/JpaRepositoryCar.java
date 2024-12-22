package com.pablords.parking.adapters.outbound.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.models.CarModel;

@Repository
public interface JpaRepositoryCar extends JpaRepository<CarModel, Long> {}
