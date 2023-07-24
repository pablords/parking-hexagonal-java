package com.pablords.parking.core.models.dtos;

import com.pablords.parking.core.valuesObjects.Plate;

public class CreateCarDto {
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
