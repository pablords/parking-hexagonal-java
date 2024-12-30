package com.pablords.parking.adapters.inbound.http.mappers;


import com.pablords.parking.adapters.inbound.http.dtos.CheckinRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.valueObjects.Plate;


public class CheckinMapper {
    private CheckinMapper() {
        // private constructor to hide the implicit public one
    }

    public static Car toEntity(CheckinRequestDTO chekinRequestDTO) {
        Car car = new Car();
        var plate = new Plate(chekinRequestDTO.getPlate());
        car.setPlate(plate);
        car.setBrand(chekinRequestDTO.getBrand());
        car.setColor(chekinRequestDTO.getColor());
        car.setModel(chekinRequestDTO.getModel());
        return car;
    }

    public static CheckinResponseDTO toResponse(Checkin checkin) {
        var chekinResponse = new CheckinResponseDTO();
        var slot = checkin.getSlot();
        chekinResponse.setSlot(slot);
        chekinResponse.setCheckInTime(checkin.getCheckInTime());
        chekinResponse.setId(checkin.getId());
        return chekinResponse;
    }
}
