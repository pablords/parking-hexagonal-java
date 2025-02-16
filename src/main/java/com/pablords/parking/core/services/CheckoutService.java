package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.CarNotFoundException;
import com.pablords.parking.core.exceptions.CheckinNotFoundException;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.ports.inbound.services.CheckoutServicePort;
import com.pablords.parking.core.ports.outbound.producers.CheckoutProducerPort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutService implements CheckoutServicePort {

    private final CheckinRepositoryPort checkinRepository;
    private final CheckoutRepositoryPort checkoutRepository;
    private final SlotRepositoryPort slotRepository;
    private final CarRepositoryPort carRepository;
    private final CheckoutProducerPort checkoutProducer;

    public CheckoutService(
            CheckinRepositoryPort checkinRepository,
            CheckoutRepositoryPort checkoutRepository,
            SlotRepositoryPort slotRepository,
            CarRepositoryPort carRepository,
            CheckoutProducerPort checkoutProducer) {
        this.checkinRepository = checkinRepository;
        this.checkoutRepository = checkoutRepository;
        this.slotRepository = slotRepository;
        this.carRepository = carRepository;
        this.checkoutProducer = checkoutProducer;
    }

    public Checkout checkout(String plate) {
        var checkinByPlate = checkinRepository.findByPlate(plate)
                .orElseThrow(() -> new CheckinNotFoundException(
                        String.format(ErrorMessages.CHECKIN_NOT_FOUND_BY_PLATE, plate)));
        var checkinById = checkinRepository.findById(checkinByPlate.getId())
                .orElseThrow(() -> new CheckinNotFoundException(
                        String.format(ErrorMessages.CHECKIN_NOT_FOUND_BY_ID, checkinByPlate.getId())));
        var carByPlate = carRepository.findByPlate(plate)
                .orElseThrow(
                        () -> new CarNotFoundException(String.format(ErrorMessages.CAR_NOT_FOUND_BY_PLATE, plate)));

        if (checkinById.getCheckInTime() == null) {
            throw new CheckinTimeMissingException(ErrorMessages.CHECKIN_TIME_IS_MISSING);
        }

        checkinById.setCar(carByPlate);

        Checkout checkout = new Checkout(checkinById);

        Slot slot = checkinById.getSlot();
        slot.free(); // Libera a vaga

        slotRepository.save(slot); // Atualiza a vaga
        checkinRepository.save(checkinById); // Atualiza a checkin
        this.sendCheckoutMessage(checkout); // Envia a mensagem
        return checkoutRepository.save(checkout); // Salva o checkout

    }

    public void sendCheckoutMessage(Checkout checkout) {
        try {
            checkoutProducer.sendCheckoutMessage(checkout);
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem: {}", e.getMessage());
        }
    }
}
