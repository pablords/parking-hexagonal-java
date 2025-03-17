package com.pablords.parking.contract;

import au.com.dius.pact.provider.junit5.*;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.parking.adapters.inbound.http.handlers.GlobalExceptionHandler;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.InvalidPlateException;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.valueobjects.Plate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("ParkingService")
// Caminho para os contratos "mockados"
@PactFolder("src/test/resources/contracts")
// @PactBroker(url = "http://localhost:9292", // URL do Pact Broker
// authentication = @PactBrokerAuth(username = "admin", password = "password")
// // Se necessário autenticação
// )
@ActiveProfiles("contract-test")
class ProviderContractTest {

  @LocalServerPort
  int port;

  @MockBean
  private CarRepositoryPort carRepositoryPort; // Mock do repositório

  @MockBean
  private CheckinRepositoryPort checkinRepositoryPort; // Mock do repositório

  @MockBean
  private CheckoutRepositoryPort checkoutRepositoryPort; // Mock do repositório

  @MockBean
  private SlotRepositoryPort slotRepositoryPort; // Mock do repositório

  @MockBean
  private GlobalExceptionHandler exceptionHandler;

  Checkin checkin;
  Checkout checkout;
  Slot slot;
  Car car;
  Clock checkoutFixedClock, exceptionFixedClock;
  ZoneId zoneId = ZoneId.of("UTC");

  @BeforeEach
  void setup() {
    car = new Car(new Plate("ABC1234"), "Toyota", "Red", "Sedan");
    car.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
    slot = new Slot(1l, false);
    checkin = new Checkin(slot, car);
    checkin.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
    checkin.setCheckInTime(LocalDateTime.parse("2025-03-13T13:00:00"));
    checkin.setCheckOutTime(LocalDateTime.parse("2025-03-13T14:00:00"));

    // Definir um horário fixo para check-in e check-out
    Instant fixedCheckout = Instant.parse("2025-03-13T14:00:00Z");
    checkoutFixedClock = Clock.fixed(fixedCheckout, zoneId);

    Instant fixedException = Instant.parse("2025-03-13T14:00:00Z");
    exceptionFixedClock = Clock.fixed(fixedException, zoneId);
  }

  @State("Não existe um carro cadastrado com a placa ABC1234")
  public void setupCar() {
    System.out.println("Configurando o estado: Carro cadastrado no sistema");
    when(carRepositoryPort.findByPlate("ABC1234"))
        .thenReturn(Optional.of(car));
    when(carRepositoryPort.save(any(Car.class)))
        .thenReturn(car);
  }

  @State("Existe uma vaga disponível")
  public void setupCheckin() {
    System.out.println("Configurando o estado: Carro cadastrado com a placa ABC1234 e uma vaga disponível");
    when(carRepositoryPort.findByPlate("ABC1234"))
        .thenReturn(Optional.of(car));
    when(carRepositoryPort.save(any(Car.class)))
        .thenReturn(car);
    when(checkinRepositoryPort.findByPlate("ABC1234"))
        .thenReturn(Optional.of(checkin));
    when(checkinRepositoryPort.save(any(Checkin.class)))
        .thenReturn(checkin);
    when(slotRepositoryPort.findAvailableSlot())
        .thenReturn(Optional.of(slot));
    when(slotRepositoryPort.save(slot))
        .thenReturn(slot);

    checkout = new Checkout(checkin, checkoutFixedClock);
    checkout.setParkingFee(2.5);
    when(checkoutRepositoryPort.findByCheckinId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b")))
        .thenReturn(Optional.of(checkout));

  }

  @State("Existe um carro estacionado com a placa ABC1234 e pronto para checkout")
  public void setupCheckout() {
    System.out.println("Configurando o estado: Carro está estacionado com a placa ABC1234 e pronto para checkout");
    when(carRepositoryPort.findByPlate("ABC1234"))
        .thenReturn(Optional.of(car));
    when(carRepositoryPort.save(any(Car.class)))
        .thenReturn(car);
    when(checkinRepositoryPort.findByPlate("ABC1234"))
        .thenReturn(Optional.of(checkin));
    when(checkinRepositoryPort.save(any(Checkin.class)))
        .thenReturn(checkin);

    checkout = new Checkout(checkin, checkoutFixedClock);
    checkout.setParkingFee(2.5);
    when(checkoutRepositoryPort.findByCheckinId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b")))
        .thenReturn(Optional.of(checkout));
    when(checkoutRepositoryPort.save(any(Checkout.class)))
        .thenReturn(checkout);
  }

  @State("Tentativa de check-in com placa vazia")
  public void setupEmptyPlate() {
    System.out.println("Configurando o estado: Tentativa de check-in com placa vazia");
    when(exceptionHandler.handleValidationExceptions(any(), any()))
        .thenAnswer(invocation -> {
          return new GlobalExceptionHandler(exceptionFixedClock).handleValidationExceptions(invocation.getArgument(0),
              invocation.getArgument(1));
        });
  }

  @State("Tentativa de check-in com placa inválida")
  public void setupInvalidPlate() {
    System.out.println("Configurando o estado: Tentativa de check-in com placa inválida");
    when(exceptionHandler.handleRuntimeException(any(InvalidPlateException.class), any(HttpServletRequest.class)))
        .thenAnswer(invocation -> {
          HttpServletRequest request = invocation.getArgument(1);
          return new GlobalExceptionHandler(exceptionFixedClock).handleRuntimeException(invocation.getArgument(0),
              request);
        });
  }

  @State("Tentativa de check-out sem data de check-in")
  public void setupCheckoutWithoutCheckin() {
    checkin.setCheckInTime(null);
    when(carRepositoryPort.findByPlate("ABC1234"))
        .thenReturn(Optional.of(car));
    when(carRepositoryPort.save(any(Car.class)))
        .thenReturn(car);
    when(checkinRepositoryPort.findByPlate("ABC1234"))
        .thenReturn(Optional.of(checkin));
    when(checkinRepositoryPort.save(any(Checkin.class)))
        .thenReturn(checkin);
    System.out.println("Configurando o estado: Tentativa de check-out sem check-in");
    when(exceptionHandler.handleRuntimeException(any(RuntimeException.class), any(HttpServletRequest.class)))
        .thenAnswer(invocation -> {
          HttpServletRequest request = invocation.getArgument(1);
          return new GlobalExceptionHandler(exceptionFixedClock).handleRuntimeException(invocation.getArgument(0),
              request);
        });
  }

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void validatePactContract(PactVerificationContext context) {
    context.setTarget(new HttpTestTarget("localhost", port));
    context.verifyInteraction();
  }
}
