package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.exceptions.SlotOccupiedException;


public class SlotService {
    private final SlotRepositoryPort slotRepository;

    public SlotService(SlotRepositoryPort slotRepository) {
        this.slotRepository = slotRepository;
    }

    public Slot occupySlot(Slot slot) {
        if (slot.isOccupied()) {
            throw new SlotOccupiedException();
        }
        slot.occupy();
        return slotRepository.save(slot);
    }

    public void freeSlot(Slot slot) {
        slot.free();
        slotRepository.save(slot);
    }

    public Slot findAvailableSlot() {
        return slotRepository.findAvailableSlot()
                .orElseThrow(() -> new ParkingFullException());
    }
}
