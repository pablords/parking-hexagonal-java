package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;


public class CheckinService {
    private final CheckinRepositoryPort checkinRepository;
    private final SlotRepositoryPort slotRepository;

    public CheckinService(CheckinRepositoryPort checkinRepository, SlotRepositoryPort slotRepository) {
        this.checkinRepository = checkinRepository;
        this.slotRepository = slotRepository;
    }

    public Checkin checkIn(Car car) {
        Slot availableSlot = slotRepository.findAvailableSlot()
                .orElseThrow(() -> new ParkingFullException());

        Checkin checkin = new Checkin(availableSlot, car);
        availableSlot.occupy();  // Marca a vaga como ocupada
        slotRepository.save(availableSlot);  // Atualiza a vaga
        return checkinRepository.save(checkin);  // Salva o checkin
    }
}
