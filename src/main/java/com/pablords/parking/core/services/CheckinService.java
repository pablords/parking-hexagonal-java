package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.exceptions.InvalidCheckinException;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckinService implements CheckinServicePort {
    private final CheckinRepositoryPort checkinRepository;
    private final SlotRepositoryPort slotRepository;
    private final CheckoutRepositoryPort checkoutRepository;

    public CheckinService(CheckinRepositoryPort checkinRepository,
            SlotRepositoryPort slotRepository,
            CheckoutRepositoryPort checkoutRepository) {
        this.checkinRepository = checkinRepository;
        this.slotRepository = slotRepository;
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public Checkin checkIn(Car car) {
        log.debug("Iniciando validação do estacionamento para o carro com a placa: {}", car.getPlate().getValue());
        var availableSlot = slotRepository.findAvailableSlot()
                .orElseThrow(() -> {
                    log.warn("Estacionamento está cheio. Não é possível estacionar o carro com a placa: {}", car.getPlate());
                    return new ParkingFullException();
                });

        var checkinByPlate = checkinRepository.findByPlate(car.getPlate().getValue()).orElse(null); 
        if (checkinByPlate != null) {
            var checkout = checkoutRepository.findByCheckinId(checkinByPlate.getId());
            if (!checkout.isPresent()) {
                throw new InvalidCheckinException(ErrorMessages.INVALID_CHECKIN_CHECKOUT_NOT_FOUND);
            }
        }
        Checkin checkin = new Checkin(availableSlot, car);
        availableSlot.setCheckin(checkin);
        availableSlot.occupy(); // Marca a vaga como ocupada
        slotRepository.save(availableSlot); // Atualiza a vaga
        log.info("Carro com a placa: {} estacionado com sucesso", car.getPlate().getValue());

        return checkinRepository.save(checkin); // Salva o checkin
    }
}
