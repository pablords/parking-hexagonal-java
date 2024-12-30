package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import java.util.Optional;

import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.core.entities.Slot;

public class SlotMapper {

    public static SlotModel toModel(Slot slot) {
        var model = new SlotModel();
        model.setId(slot.getId());
        model.setOccupied(slot.isOccupied());
        return model;
    }

    public static Slot toEntity(SlotModel model) {
        var slot = new Slot();
        slot.setId(model.getId());
        return slot;
    }
}
