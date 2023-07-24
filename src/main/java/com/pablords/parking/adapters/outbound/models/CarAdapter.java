package com.pablords.parking.adapters.outbound.models;

import com.pablords.parking.core.models.Car;
import com.pablords.parking.core.valuesObjects.Plate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "parking")
public class CarAdapter {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate")
    private String plate;

    @Column(name = "brand")
    private String brand;

    public CarAdapter(Car car) {
        this.brand = car.getBrand();
        this.plate = car.getPlate().getValue();
    }
}
