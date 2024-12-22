package com.pablords.parking.adapters.inbound.api.dtos;

import com.pablords.parking.core.valueObjects.Plate;

public class CreateCarDTO {
    Plate plate;
    String brand;

    public Plate getPlate() {
        return plate;
    }

    public void setPlate(Plate plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
