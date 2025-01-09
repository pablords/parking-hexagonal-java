package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.adapters.outbound.messaging.producers.CheckoutProducerAdapter;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.valueobjects.Plate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
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

    @InjectMocks
    private CheckoutService checkoutService;

    private Checkin checkin;
    private Slot slot;
    private Car car;
    private UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        slot = new Slot();
        var plate = new Plate("ABC1234");
        car = new Car(plate, "Honda", "Green", "Civic"); // Criar um objeto de carro com uma placa fictícia
        checkin = new Checkin(slot, car);
        checkin.setCheckInTime(LocalDateTime.now().minusHours(2)); // Simula um checkin feito há 2 horas
    }

    @Test
    void testCheckout_Success() {
        // Cenário: Realizando checkout com sucesso
        Checkout checkout = new Checkout(checkin);
        checkout.calculateParkingFee();
        checkin.setId(uuid);

        when(checkinRepository.findByPlate("ABC1234")).thenReturn(Optional.of(checkin));
        when(checkinRepository.findById(uuid)).thenReturn(Optional.of(checkin));
        when(carRepository.findByPlate("ABC1234")).thenReturn(Optional.of(car));

        when(checkoutRepository.save(any(Checkout.class))).thenReturn(checkout);
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        Checkout result = checkoutService.checkout(checkin.getCar().getPlate().getValue());

        // Verifica se a hora de saída foi registrada
        assertNotNull(result.getCheckOutTime());
        assertTrue(result.getCheckOutTime().isAfter(checkin.getCheckInTime()));

        // Verifica se a taxa de estacionamento foi calculada corretamente
        double expectedFee = 2 * 2.5; // 2 horas * 2,50 (em centavos)
        assertEquals(expectedFee, result.getParkingFee());

        // Verifica se o checkout foi salvo no repositório
        verify(checkoutRepository, times(1)).save(any(Checkout.class));

        // Verifica se a vaga foi liberada e atualizada
        verify(slotRepository, times(1)).save(any(Slot.class));
        assertTrue(!slot.isOccupied());
    }

    @Test
    void testCheckout_EmptyCheckinTime() {
        Checkout checkout = new Checkout(checkin);
        checkout.calculateParkingFee();
        checkin.setId(uuid);

        when(checkinRepository.findByPlate("ABC1234")).thenReturn(Optional.of(checkin));
        when(checkinRepository.findById(uuid)).thenReturn(Optional.of(checkin));
        when(carRepository.findByPlate("ABC1234")).thenReturn(Optional.of(car));
        // Cenário: Testa o caso em que o checkin não tem hora registrada (deve dar erro
        // ou não permitir o checkout)
        checkin.setCheckInTime(null);

        String plateValue = checkin.getCar().getPlate().getValue();
        CheckinTimeMissingException thrown = assertThrows(CheckinTimeMissingException.class,
                () -> checkoutService.checkout(plateValue));
        assertEquals("Checkin time is missing", thrown.getMessage());
    }

    @Test
    void testCheckout_UpdatesSlotAvailability() {
        Checkout checkout = new Checkout(checkin);
        checkout.calculateParkingFee();
        checkin.setId(uuid);

        when(checkinRepository.findByPlate("ABC1234")).thenReturn(Optional.of(checkin));
        when(checkinRepository.findById(uuid)).thenReturn(Optional.of(checkin));
        when(carRepository.findByPlate("ABC1234")).thenReturn(Optional.of(car));

        // Cenário: Verifica se a vaga é liberada corretamente
        when(checkoutRepository.save(any(Checkout.class))).thenReturn(new Checkout(checkin));

        checkoutService.checkout(checkin.getCar().getPlate().getValue());

        // Verifica se a vaga foi liberada
        verify(slotRepository, times(1)).save(slot);
        assertTrue(!slot.isOccupied());
    }
}
