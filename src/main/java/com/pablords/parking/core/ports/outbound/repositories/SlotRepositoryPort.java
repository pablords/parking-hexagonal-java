package com.pablords.parking.core.ports.outbound.repositories;

import com.pablords.parking.core.entities.Slot;

import java.util.Optional;

public interface SlotRepositoryPort {
    Optional<Slot> findAvailableSlot();

    Slot save(Slot slot);

    Optional<Slot> findById(Long id); // Vaga disponível
}
