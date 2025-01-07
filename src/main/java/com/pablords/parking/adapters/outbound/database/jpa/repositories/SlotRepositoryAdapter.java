package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.SlotMapper;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SlotRepositoryAdapter implements SlotRepositoryPort {

    private final JpaRepositorySlot jpaRepositorySlot;

    public SlotRepositoryAdapter(JpaRepositorySlot jpaRepositorySlot) {
        this.jpaRepositorySlot = jpaRepositorySlot;
    }

    @Override
    public Optional<Slot> findAvailableSlot() {
        var slotModel = jpaRepositorySlot.findFirstByOccupiedFalse();
        return slotModel.map(SlotMapper::toEntity);
    }

    @Override
    public Slot save(Slot slot) {
        var createdSlot = jpaRepositorySlot.save(SlotMapper.toModel(slot));
        return SlotMapper.toEntity(createdSlot);
    }

    @Override
    public Optional<Slot> findById(Long id) {
        var slot = jpaRepositorySlot.findById(id);
        return slot.map(SlotMapper::toEntity);
    }
}
