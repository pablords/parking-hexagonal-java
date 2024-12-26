package com.pablords.parking.adapters.outbound.mappers;
import com.pablords.parking.adapters.outbound.models.CarModel;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.valueObjects.Plate;

public class CarMapper {

    public static CarModel toModel(Car car) {
        CarModel model = new CarModel();
        model.setId(car.getId());
        model.setBrand(car.getBrand());
        model.setPlate(car.getPlate().getValue());
        model.setColor(car.getColor());
        model.setModel(car.getModel());
        return model;
    }

    public static Car toEntity(CarModel model) {
        Car car = new Car();
        var plate = new Plate(model.getPlate());
        car.setId(model.getId());
        car.setBrand(model.getBrand());
        car.setPlate(plate);
        car.setColor(model.getColor());
        car.setModel(model.getModel());
        return car;
    }
}
