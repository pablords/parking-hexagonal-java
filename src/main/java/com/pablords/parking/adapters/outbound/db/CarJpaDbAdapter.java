package com.pablords.parking.adapters.outbound.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.models.CarAdapter;

@Repository
public interface CarJpaDbAdapter extends JpaRepository<CarAdapter, Long> {}
