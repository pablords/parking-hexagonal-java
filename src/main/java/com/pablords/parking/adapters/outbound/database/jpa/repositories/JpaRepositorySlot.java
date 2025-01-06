package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface JpaRepositorySlot extends JpaRepository<SlotModel, UUID> {
    Optional<SlotModel> findByOccupiedFalse(); // Vaga disponível
    Optional<SlotModel> findFirstByOccupiedFalse();
    List<SlotModel> findByOccupiedTrue(); // Vagas ocupadas
}
