package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import jakarta.transaction.Transactional;

public class CheckinService implements CheckinServicePort {
    private final CheckinRepositoryPort checkinRepository;
    private final SlotRepositoryPort slotRepository;
    private final CarRepositoryPort carRepository;

    public CheckinService(CheckinRepositoryPort checkinRepository,
            SlotRepositoryPort slotRepository,
            CarRepositoryPort carRepository) {
        this.checkinRepository = checkinRepository;
        this.slotRepository = slotRepository;
        this.carRepository = carRepository;
    }

    @Override
    public Checkin checkIn(Car car) {
        Slot availableSlot = slotRepository.findAvailableSlot()
                .orElseThrow(() -> new ParkingFullException());
        
        var carByPlate = carRepository.existsByPlate(car.getPlate().getValue());
        if (!carByPlate) {
            carRepository.save(car);
        }

        Checkin checkin = new Checkin(availableSlot, car);
        availableSlot.occupy(); // Marca a vaga como ocupada
        slotRepository.save(availableSlot); // Atualiza a vaga
        return checkinRepository.save(checkin); // Salva o checkin
    }
}
