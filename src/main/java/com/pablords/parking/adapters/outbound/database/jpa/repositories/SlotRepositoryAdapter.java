package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.SlotMapper;
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
        // Recupera o Optional<SlotModel> do repositório
        var slotModel = jpaRepositorySlot.findFirstByOccupiedFalse();

        // Usa map para transformar o Optional<SlotModel> em Optional<Slot> usando o
        // mapper
        return slotModel.map(SlotMapper::toEntity);
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
        var slotModel = SlotMapper.toModel(slot);
        var createdSlot = jpaRepositorySlot.save(slotModel);
        return SlotMapper.toEntity(createdSlot);
    }
}
