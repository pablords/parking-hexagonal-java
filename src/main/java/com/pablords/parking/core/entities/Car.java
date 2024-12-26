package com.pablords.parking.core.entities;

import com.pablords.parking.core.valueObjects.Plate;

public class Car {
    Long id;
    Plate plate;
    String brand;
    String color;
    String model;

    public Car() {
    }

    public Car(Plate plate, String brand, String color, String model) {
        this.plate = plate;
        this.brand = brand;
        this.color = color;
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
