package com.pablords.parking.core.services;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.valueObjects.Plate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckinServiceTest {

    @Mock
    private CheckinRepositoryPort checkinRepository;

    @Mock
    private CarRepositoryPort carRepository;

    @Mock
    private SlotRepositoryPort slotRepository;

    @InjectMocks
    private CheckinService checkinService;

    private Slot availableSlot;
    private Car car;

    @BeforeEach
    void setUp() {
        availableSlot = new Slot(); // Criar um objeto de vaga disponível
        var plate = new Plate("ABC1234");
        car = new Car(plate, "Honda", "Green", "Civic"); // Criar um objeto de carro com uma placa fictícia
    }

    @Test
    void testCheckIn_Success() {
        // Cenário em que há uma vaga disponível
        when(slotRepository.findAvailableSlot()).thenReturn(Optional.of(availableSlot));
        when(checkinRepository.save(any(Checkin.class))).thenReturn(new Checkin(availableSlot, car));

        Checkin checkin = checkinService.checkIn(car);

        // Verifica se a vaga foi marcada como ocupada
        verify(slotRepository, times(1)).save(availableSlot);
        assertNotNull(checkin);
        assertEquals(availableSlot, checkin.getSlot());
        assertEquals(car, checkin.getCar());
    }

    @Test
    void testCheckIn_NoAvailableSlot_ThrowsParkingFullException() {
        // Cenário em que não há vaga disponível
        when(slotRepository.findAvailableSlot()).thenReturn(Optional.empty());

        // Verifica se o ParkingFullException é lançado
        ParkingFullException thrown = assertThrows(ParkingFullException.class, () -> checkinService.checkIn(car));
        assertEquals("No available parking slots", thrown.getMessage());
    }

    @Test
    void testCheckIn_UpdatesSlotToOccupied() {
        // Cenário em que a vaga é marcada como ocupada após check-in
        when(slotRepository.findAvailableSlot()).thenReturn(Optional.of(availableSlot));
        when(checkinRepository.save(any(Checkin.class))).thenReturn(new Checkin(availableSlot, car));

        checkinService.checkIn(car);

        // Verifica se o método de ocupar a vaga foi chamado
        assertTrue(availableSlot.isOccupied());
    }

    @Test
    void testCheckIn_SavesCheckin() {
        // Cenário em que o checkin é salvo
        when(slotRepository.findAvailableSlot()).thenReturn(Optional.of(availableSlot));
        when(checkinRepository.save(any(Checkin.class))).thenReturn(new Checkin(availableSlot, car));

        Checkin checkin = checkinService.checkIn(car);

        // Verifica se o método save foi chamado no repositório de checkin
        verify(checkinRepository, times(1)).save(any(Checkin.class));
        assertNotNull(checkin);
    }
}
