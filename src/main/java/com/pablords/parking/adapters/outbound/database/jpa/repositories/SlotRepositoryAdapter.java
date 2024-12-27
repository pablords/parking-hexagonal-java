package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SlotRepositoryAdapter implements SlotRepositoryPort {

    private final JpaRepositorySlot jpaRepositorySlot;

    public SlotRepositoryAdapter(JpaRepositorySlot jpaRepositorySlot) {
        this.jpaRepositorySlot = jpaRepositorySlot;
    }

    @Override
    public Optional<Slot> findAvailableSlot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAvailableSlot'");
    }

    @Override
    public List<Slot> findAvailableSlots() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAvailableSlots'");
    }

    @Override
    public List<Slot> findOccupiedSlots() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOccupiedSlots'");
    }

    @Override
    public Slot save(Slot slot) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
