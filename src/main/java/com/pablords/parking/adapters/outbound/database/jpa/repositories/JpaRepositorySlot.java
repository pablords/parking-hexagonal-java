package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.core.entities.Slot;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface JpaRepositorySlot extends JpaRepository<SlotModel, Long> {
    Optional<Slot> findByOccupiedFalse(); // Vaga disponível
    List<Slot> findByOccupiedTrue(); // Vagas ocupadas
}
