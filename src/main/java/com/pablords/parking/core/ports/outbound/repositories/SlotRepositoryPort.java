package com.pablords.parking.core.ports.outbound.repositories;

import com.pablords.parking.core.entities.Slot;

import java.util.List;
import java.util.Optional;

public interface SlotRepositoryPort {
    Optional<Slot> findAvailableSlot();
    List<Slot> findAvailableSlots();
    List<Slot> findOccupiedSlots();
    Slot save(Slot slot);
}
