package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaRepositoryCar;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.valueObjects.Plate;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckinMapper {

    private JpaRepositoryCar jRepositoryCar;

    @Autowired
    public CheckinMapper(JpaRepositoryCar jRepositoryCar) {
        this.jRepositoryCar = jRepositoryCar;
    }

    public CheckinModel toModel(Checkin checkin) {
        log.debug("Mapendo o Checkin para CheckinModel: {}", checkin.toString());
        var model = new CheckinModel();
        model.setId(checkin.getId());
        model.setSlot(SlotMapper.toModel(checkin.getSlot()));
        model.setCheckInTime(checkin.getCheckInTime());
        model.setCheckOutTime(checkin.getCheckOutTime());

        // Recupera o modelo JPA do carro para evitar instâncias transientes
        CarModel carModel = jRepositoryCar.findByPlate(checkin.getCar().getPlate().getValue())
                .orElse(null);
        model.setCar(carModel);

        if(carModel == null) {
            model.setCar(CarMapper.toModel(checkin.getCar()));
        }

        return model;
    }

    public static Checkin toEntity(CheckinModel checkinModel) {
        log.debug("Mapendo o CheckinModel para Checkin: {}", checkinModel.toString());
        var car = new Car();
        var parsedPlate = new Plate(checkinModel.getCar().getPlate());
        car.setPlate(parsedPlate);
        var checkin = new Checkin(SlotMapper.toEntity(checkinModel.getSlot()), car);
        checkin.setId(checkinModel.getId());
        checkin.setSlot(SlotMapper.toEntity(checkinModel.getSlot()));
        return checkin;
    }
}
