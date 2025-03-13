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
    log.info("Iniciando estacionamento para o carro com a placa: {}", car.getPlate().getValue());
    var availableSlot = slotRepository.findAvailableSlot()
        .orElseThrow(() -> {
          log.warn("Estacionamento está cheio. Não é possível estacionar o carro com a placa: {}",
              car.getPlate());
          return new ParkingFullException();
        });

    var checkinByPlate = checkinRepository.findByPlate(car.getPlate().getValue()).orElse(null);
    if (checkinByPlate != null) {
      log.info("PLaca {} já está estacionada", car.getPlate().getValue());
      var checkout = checkoutRepository.findByCheckinId(checkinByPlate.getId());
      if (!checkout.isPresent()) {
        log.warn("Carro com a placa: {} já está estacionado e não foi feito checkout", car.getPlate().getValue());
        throw new InvalidCheckinException(ErrorMessages.INVALID_CHECKIN_CHECKOUT_NOT_FOUND);
      }
    }

    availableSlot.occupy(); // Marca a vaga como ocupada
    var savedSlot = slotRepository.save(availableSlot); // Atualiza a vaga
    log.info("Vaga {} ocupada", savedSlot.getId());

    // Salva o carro (delegado ao adaptador, que lida com persistência)
    var savedCar = carRepository.save(car);
    log.info("Carro com a placa: {} salvo com sucesso", savedCar.getPlate().getValue());

    var checkin = new Checkin(availableSlot, car);
    log.info("Criado checkin para o carro com a placa: {}", car.getPlate().getValue());
    var checkinSaved = checkinRepository.save(checkin); // Salva o checkin

    log.info("Carro com a placa: {} estacionado com sucesso", checkinSaved.getCar().getPlate().getValue());
    return checkinSaved;
  }
}
