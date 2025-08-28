package com.pablords.parking.unit.core.services;

import com.pablords.parking.adapters.outbound.messaging.producers.CheckoutProducerAdapter;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.services.CheckoutService;
import com.pablords.parking.core.valueobjects.Plate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceUnitTest {

  @Mock
  private CheckoutProducerAdapter checkoutProducerAdapter;

  @Mock
  private CheckinRepositoryPort checkinRepository;

  @Mock
  private CheckoutRepositoryPort checkoutRepository;

  @Mock
  private SlotRepositoryPort slotRepository;

  @Mock
  private CarRepositoryPort carRepository;


  private CheckoutService checkoutService;

  private Checkin checkin;
  private Slot slot;
  private Car car;
  private UUID uuid;

  private final static LocalDateTime CHECKIN_TIME = LocalDateTime.of(2025, 8, 1, 8, 0, 42, 956738000);
  private static final LocalDateTime CHECKOUT_TIME = LocalDateTime.of(2025, 8, 1, 10, 0, 42, 956738000);
  ZoneId zone = ZoneId.systemDefault();
  Clock checkoutClock = Clock.fixed(CHECKOUT_TIME.atZone(zone).toInstant(), zone);

  @BeforeEach
  void setUp() {
    uuid = UUID.randomUUID();
    slot = new Slot();
    var plate = new Plate("ABC1234");
    car = new Car(plate, "Honda", "Green", "Civic");
    checkin = new Checkin(slot, car);
    checkin.setCheckInTime(CHECKIN_TIME);

    checkoutService = new CheckoutService(checkinRepository, checkoutRepository, slotRepository, carRepository, checkoutProducerAdapter, checkoutClock);
  }

  @Nested
  @DisplayName("Testes de Checkout bem-sucedido")
  class SuccessfulCheckoutTests {

    @Test
    @DisplayName("Deve realizar o checkout com sucesso")
    void shouldCheckoutSuccessfully() {
      checkin.setId(uuid);
      Checkout checkout = new Checkout(checkin, checkoutClock);

      when(checkinRepository.findByPlate("ABC1234")).thenReturn(Optional.of(checkin));
      when(carRepository.findByPlate("ABC1234")).thenReturn(Optional.of(car));
      when(checkoutRepository.save(any(Checkout.class))).thenReturn(checkout);
      when(slotRepository.save(any(Slot.class))).thenReturn(slot);

      Checkout result = checkoutService.checkout(checkin.getCar().getPlate().getValue());

      assertNotNull(result.getCheckOutTime());
      assertTrue(result.getCheckOutTime().isAfter(checkin.getCheckInTime()));

      double expectedFee = 2 * 2.5;
      assertEquals(expectedFee, result.getParkingFee());

      verify(checkoutRepository, times(1)).save(any(Checkout.class));
      verify(slotRepository, times(1)).save(any(Slot.class));
      assertFalse(slot.isOccupied());
    }
  }

  @Nested
  @DisplayName("Testes de falha no Checkout")
  class FailedCheckoutTests {

    @Test
    @DisplayName("Deve lançar CheckinTimeMissingException quando o check-in não tiver horário registrado")
    void shouldThrowExceptionWhenCheckinTimeIsMissing() {
      checkin.setId(uuid);
      checkin.setCheckInTime(null);

      when(checkinRepository.findByPlate("ABC1234")).thenReturn(Optional.of(checkin));
      when(carRepository.findByPlate("ABC1234")).thenReturn(Optional.of(car));

      String plateValue = checkin.getCar().getPlate().getValue();
      CheckinTimeMissingException thrown = assertThrows(CheckinTimeMissingException.class,
          () -> checkoutService.checkout(plateValue));
      assertEquals("Checkin time is missing", thrown.getMessage());
    }
  }

  @Nested
  @DisplayName("Testes de atualização de disponibilidade da vaga")
  class SlotAvailabilityTests {

    @Test
    @DisplayName("Deve liberar a vaga corretamente após checkout")
    void shouldReleaseSlotAfterCheckout() {
      checkin.setId(uuid);
      Checkout checkout = new Checkout(checkin, checkoutClock);

      when(checkinRepository.findByPlate("ABC1234")).thenReturn(Optional.of(checkin));
      when(carRepository.findByPlate("ABC1234")).thenReturn(Optional.of(car));
      when(checkoutRepository.save(any(Checkout.class))).thenReturn(checkout);

      checkoutService.checkout(checkin.getCar().getPlate().getValue());

      verify(slotRepository, times(1)).save(slot);
      assertFalse(slot.isOccupied());
    }
  }
}
