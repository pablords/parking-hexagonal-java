package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.valueObjects.Plate;

public class CheckinMapper {

    public static CheckinModel toModel(Checkin checkin) {
        var model = new CheckinModel();
        model.setId(checkin.getId());
        model.setSlotId(checkin.getSlot().getId());
        model.setCheckInTime(checkin.getCheckInTime());
        model.setCheckOutTime(checkin.getCheckOutTime());
        model.setCar(CarMapper.toModel(checkin.getCar()));
        return model;
    }

    public static Checkin toEntity(CheckinModel checkinModel) {
        var slot = new Slot();
        slot.setId(checkinModel.getId());
        var car = new Car();
        var parsedPlate = new Plate(checkinModel.getCar().getPlate());
        car.setPlate(parsedPlate);
        var checkin = new Checkin(slot, car);
        checkin.setId(checkinModel.getId());
        return checkin;
    }
}
