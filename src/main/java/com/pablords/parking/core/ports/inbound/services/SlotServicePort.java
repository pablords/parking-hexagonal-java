package com.pablords.parking.core.ports.inbound.services;

import java.util.List;

import com.pablords.parking.core.entities.Slot;

public interface SlotServicePort {
    Slot occupySlot(Slot slot);
    void freeSlot(Slot slot);
    List<Slot> findAvailableSlots();
    List<Slot> findOccupiedSlots();
    Slot findAvailableSlot();
}
