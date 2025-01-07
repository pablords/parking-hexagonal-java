package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.CarNotFoundException;
import com.pablords.parking.core.exceptions.CheckinNotFoundException;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.ports.inbound.services.CheckoutServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class CheckoutService implements CheckoutServicePort {
    private static final long HOURLY_RATE_IN_CENTS = 250;

    private final CheckinRepositoryPort checkinRepository;
    private final CheckoutRepositoryPort checkoutRepository;
    private final SlotRepositoryPort slotRepository;
    private final CarRepositoryPort carRepository;

    public CheckoutService(
            CheckinRepositoryPort checkinRepository,
            CheckoutRepositoryPort checkoutRepository,
            SlotRepositoryPort slotRepository,
            CarRepositoryPort carRepository) {
        this.checkinRepository = checkinRepository;
        this.checkoutRepository = checkoutRepository;
        this.slotRepository = slotRepository;
        this.carRepository = carRepository;
    }

    public Checkout checkout(String plate) {
        var checkinByPlate = checkinRepository.findByPlate(plate)
                .orElseThrow(() -> new CheckinNotFoundException(
                        String.format(ErrorMessages.CHECKIN_NOT_FOUND_BY_PLATE, plate)));
        var chekinById = checkinRepository.findById(checkinByPlate.getId())
                .orElseThrow(() -> new CheckinNotFoundException(
                        String.format(ErrorMessages.CHECKIN_NOT_FOUND_BY_ID, checkinByPlate.getId())));
        var carByPlate = carRepository.findByPlate(plate)
                .orElseThrow(
                        () -> new CarNotFoundException(String.format(ErrorMessages.CAR_NOT_FOUND_BY_PLATE, plate)));

        if (chekinById.getCheckInTime() == null) {
            throw new CheckinTimeMissingException(ErrorMessages.CHECKIN_TIME_IS_MISSING);
        }
        chekinById.setCheckOutTime(LocalDateTime.now()); // Registra a hora de saída
        chekinById.setCar(carByPlate);

        long parkingFee = calculateParkingFee(chekinById);
        Checkout checkout = new Checkout(chekinById);

        checkout.setParkingFee(parkingFee);

        Slot slot = chekinById.getSlot();
        slot.free(); // Libera a vaga

        slotRepository.save(slot); // Atualiza a vaga
        checkinRepository.save(chekinById); // Atualiza a checkin

        return checkoutRepository.save(checkout); // Salva o checkout

    }

    private long calculateParkingFee(Checkin checkin) {
        LocalDateTime checkInTime = checkin.getCheckInTime();
        if (checkInTime == null) {
            throw new CheckinTimeMissingException(ErrorMessages.CHECKIN_TIME_IS_MISSING);
        }


        // Calcula a duração total em segundos
        long seconds = java.time.Duration.between(checkInTime, LocalDateTime.now()).getSeconds();

        // Converte segundos para minutos (arredondando para cima, garantindo cobrança
        // mínima de 1 minuto)
        long minutes = (long) Math.ceil(seconds / 60.0);

        // Converte a taxa horária para minutos (2,50 por 60 minutos)
        double ratePerMinute = (double) HOURLY_RATE_IN_CENTS / 60;

        // Calcula a taxa total
        long totalFee = Math.round(minutes * ratePerMinute);

        log.info(String.format("Check-in: %s, Total segundos: %d, Total minutos: %d, Média por minuto: %.2f, Taxa total em centavos: %d%n",
                checkInTime, seconds, minutes, ratePerMinute, totalFee));

        return totalFee;
    }
}
