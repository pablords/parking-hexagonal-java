package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.exceptions.InvalidCheckinException;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckinService implements CheckinServicePort {
  private final CheckinRepositoryPort checkinRepository;
  private final SlotRepositoryPort slotRepository;
  private final CheckoutRepositoryPort checkoutRepository;
  private final CarRepositoryPort carRepository;

  public CheckinService(CheckinRepositoryPort checkinRepository,
      SlotRepositoryPort slotRepository,
      CheckoutRepositoryPort checkoutRepository,
      CarRepositoryPort carRepository) {
    this.checkinRepository = checkinRepository;
    this.slotRepository = slotRepository;
    this.checkoutRepository = checkoutRepository;
    this.carRepository = carRepository;
  }

  @Override
  public Checkin checkIn(Car car) {
    var plateValue = car.getPlate().getValue();
    log.info("Iniciando estacionamento para o carro com a placa: {}", plateValue);

    validateCheckinDuplicated(plateValue);

    var availableSlot = slotRepository.findAvailableSlot()
        .orElseThrow(() -> {
          log.warn("Estacionamento cheio. Não é possível estacionar o carro com a placa: {}", plateValue);
          return new ParkingFullException();
        });

    return checkinRegister(car, availableSlot);
  }

  private void validateCheckinDuplicated(String plateValue) {
    checkinRepository.findByPlate(plateValue).ifPresent(checkin -> {
      log.info("Placa {} já está estacionada", plateValue);
      checkoutRepository.findByCheckinId(checkin.getId()).ifPresentOrElse(
          checkout -> log.info("Checkout encontrado para a placa {}", plateValue),
          () -> {
            log.warn("Carro com a placa {} já está estacionado e não foi feito checkout", plateValue);
            throw new InvalidCheckinException(ErrorMessages.INVALID_CHECKIN_CHECKOUT_NOT_FOUND);
          });
    });
  }

  private Checkin checkinRegister(Car car, Slot availableSlot) {
    availableSlot.occupy();
    slotRepository.save(availableSlot);
    log.info("Vaga {} ocupada", availableSlot.getId());

    var savedCar = carRepository.save(car);
    log.info("Carro com a placa {} salvo com sucesso", savedCar.getPlate().getValue());

    var checkin = new Checkin(availableSlot, savedCar);
    var checkinSaved = checkinRepository.save(checkin);

    log.info("Carro com a placa {} estacionado com sucesso", checkinSaved.getCar().getPlate().getValue());
    return checkinSaved;
  }
}
