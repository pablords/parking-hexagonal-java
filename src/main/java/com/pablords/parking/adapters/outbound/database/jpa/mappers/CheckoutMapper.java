package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class CheckoutMapper {
    public static CheckoutModel toModel(Checkout checkout) {
        log.debug("ENTITY: {}", checkout.toString());
        var model = new CheckoutModel();
        model.setId(checkout.getId());
        model.setCheckOutTime(checkout.getCheckOutTime());
        model.setParkingFee(checkout.getParkingFee());
        model.setCheckinId(checkout.getCheckin().getId());
        return model;
    }

    public static Checkout toEntity(CheckoutModel model, Checkin checkin){
        log.debug("MODEL: {}", model.toString());
        var checkout = new Checkout(checkin);
        checkout.setId(model.getId());
        checkout.setParkingFee(model.getParkingFee());
        return checkout;
    }
}
