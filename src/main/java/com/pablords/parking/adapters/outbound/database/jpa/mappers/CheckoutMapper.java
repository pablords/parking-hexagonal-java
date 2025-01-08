package com.pablords.parking.adapters.outbound.database.jpa.mappers;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;




public class CheckoutMapper {
    public static CheckoutModel toModel(Checkout checkout) {
        var model = new CheckoutModel();
        model.setId(checkout.getId());
        model.setCheckOutTime(checkout.getCheckOutTime());
        model.setParkingFee(checkout.getParkingFee());
        model.setCheckinId(checkout.getCheckin().getId());
        return model;
    }

    public static Checkout toEntity(CheckoutModel model, Checkin checkin){
        var checkout = new Checkout(checkin);
        checkout.setId(model.getId());
        checkout.calculateParkingFee();
        return checkout;
    }
}
