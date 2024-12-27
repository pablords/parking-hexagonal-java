package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import java.time.LocalDateTime;

public class CheckoutService {
    private final CheckinRepositoryPort checkinRepository;
    private final CheckoutRepositoryPort checkoutRepository;
    private final SlotRepositoryPort slotRepository;

    public CheckoutService(CheckinRepositoryPort checkinRepository, CheckoutRepositoryPort checkoutRepository,
            SlotRepositoryPort slotRepository) {
        this.checkinRepository = checkinRepository;
        this.checkoutRepository = checkoutRepository;
        this.slotRepository = slotRepository;
    }

    public Checkout checkout(Checkin checkin) {
        if (checkin.getCheckInTime() == null) {
            throw new CheckinTimeMissingException();
        }
        checkin.setCheckOutTime(LocalDateTime.now()); // Registra a hora de saída

        long parkingFee = calculateParkingFee(checkin);
        Checkout checkout = new Checkout(checkin);
        checkout.setParkingFee(parkingFee);
        checkoutRepository.save(checkout); // Salva o checkout

        Slot slot = checkin.getSlot();
        slot.free(); // Libera a vaga
        slotRepository.save(slot); // Atualiza a vaga

        return checkout;
    }

    private long calculateParkingFee(Checkin checkin) {
        long hours = java.time.Duration.between(checkin.getCheckInTime(), LocalDateTime.now()).toHours();
        return hours * 250; // Cobrança de 2,50 por hora, multiplicado por 100 para representar centavos
    }
}
