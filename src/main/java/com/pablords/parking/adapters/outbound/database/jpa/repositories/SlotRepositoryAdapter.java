package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.SlotMapper;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SlotRepositoryAdapter implements SlotRepositoryPort {

    private final JpaSlotRepository jpaRepositorySlot;
    private final SlotMapper slotMapper;

    public SlotRepositoryAdapter(JpaSlotRepository jpaRepositorySlot, SlotMapper slotMapper) {
        this.jpaRepositorySlot = jpaRepositorySlot;
        this.slotMapper = slotMapper;
    }

    @Override
    public Optional<Slot> findAvailableSlot() {
        var slotModel = jpaRepositorySlot.findFirstByOccupiedFalse();
        return slotModel.map(slotMapper::toEntity);
    }

    @Override
    public Slot save(Slot slot) {
        var createdSlot = jpaRepositorySlot.save(slotMapper.toModel(slot));
        return slotMapper.toEntity(createdSlot);
    }

    @Override
    public Optional<Slot> findById(Long id) {
        var slot = jpaRepositorySlot.findById(id);
        return slot.map(slotMapper::toEntity);
    }
}
