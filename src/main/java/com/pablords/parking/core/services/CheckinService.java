package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.exceptions.InvalidCheckinException;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

public class CheckinService implements CheckinServicePort {
    private final CheckinRepositoryPort checkinRepository;
    private final SlotRepositoryPort slotRepository;
    private final CarRepositoryPort carRepository;
    private final CheckoutRepositoryPort checkoutRepository;

    public CheckinService(CheckinRepositoryPort checkinRepository,
            SlotRepositoryPort slotRepository,
            CarRepositoryPort carRepository,
            CheckoutRepositoryPort checkoutRepository) {
        this.checkinRepository = checkinRepository;
        this.slotRepository = slotRepository;
        this.carRepository = carRepository;
        this.checkoutRepository = checkoutRepository;
    }

   
    @Override
    public Checkin checkIn(Car car) {
        var availableSlot = slotRepository.findAvailableSlot()
                .orElseThrow(() -> new ParkingFullException());

        var checkinByPlate = checkinRepository.findByPlate(car.getPlate().getValue()).orElse(null); 

        if (checkinByPlate != null) {
            var checkout = checkoutRepository.findByCheckinId(checkinByPlate.getId());
            if (!checkout.isPresent()) {
                throw new InvalidCheckinException(ErrorMessages.INVALID_CHECKIN_CHECKOUT_NOT_FOUND);
            }
        }

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
