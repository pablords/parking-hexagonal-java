package com.pablords.parking.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;
import com.pablords.parking.core.ports.inbound.services.CheckoutServicePort;
import com.pablords.parking.core.ports.outbound.producers.CheckoutProducerPort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.services.CarService;
import com.pablords.parking.core.services.CheckinService;
import com.pablords.parking.core.services.CheckoutService;

@Configuration
public class BeanConfiguration {

  @Bean
  CarServicePort carService(CarRepositoryPort carRepositoryPort) {
    return new CarService(carRepositoryPort);
  }

  @Bean
  CheckinServicePort checkinService(
      CheckinRepositoryPort checkinRepositoryPort,
      SlotRepositoryPort slotRepository,
      CarRepositoryPort carRepository,
      CheckoutRepositoryPort checkoutRepository,
      CarRepositoryPort carRepositoryPort) {
    return new CheckinService(
        checkinRepositoryPort,
        slotRepository,
        checkoutRepository,
        carRepository);
  }

  @Bean
  CheckoutServicePort checkoutService(CheckinRepositoryPort checkinRepository,
      CheckoutRepositoryPort checkoutRepository,
      SlotRepositoryPort slotRepository,
      CarRepositoryPort carRepository,
      CheckoutProducerPort checkoutProducer,
      Clock clock) {
    return new CheckoutService(checkinRepository, checkoutRepository, slotRepository, carRepository, checkoutProducer, clock);
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone(); // Usa o relógio do sistema
  }

}
