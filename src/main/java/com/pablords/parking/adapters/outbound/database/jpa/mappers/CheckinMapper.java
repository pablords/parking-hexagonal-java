package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.valueObjects.Plate;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CheckinMapper {

    public static CheckinModel toModel(Checkin checkin) {
        log.debug("Mapendo o Checkin para CheckinModel: {}", checkin.toString());
        var model = new CheckinModel();
        model.setId(checkin.getId());
        model.setSlotId(checkin.getSlot().getId());
        model.setCheckInTime(checkin.getCheckInTime());
        model.setCheckOutTime(checkin.getCheckOutTime());
        model.setCar(CarMapper.toModel(checkin.getCar()));
        return model;
    }

    public static Checkin toEntity(CheckinModel checkinModel) {
        log.debug("Mapendo o CheckinModel para Checkin: {}", checkinModel.toString());
        var slot = new Slot();
        slot.setId(checkinModel.getSlotId());
        var car = new Car();
        var parsedPlate = new Plate(checkinModel.getCar().getPlate());
        car.setPlate(parsedPlate);
        var checkin = new Checkin(slot, car);
        checkin.setId(checkinModel.getId());
        return checkin;
    }
}
