package com.pablords.parking.unit.core.services;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.services.CheckinService;
import com.pablords.parking.core.valueobjects.Plate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class CheckinServiceUnitTest {

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
        availableSlot = new Slot(1L, false); // Criar um objeto de vaga disponível
        var plate = new Plate("ABC1234");
        car = new Car(plate, "Honda", "Green", "Civic"); // Criar um objeto de carro com uma placa fictícia

        lenient().when(slotRepository.save(any(Slot.class))).thenReturn(availableSlot);
        lenient().when(carRepository.save(any(Car.class))).thenReturn(car);
    }

    @Nested
    @DisplayName("Testes para check-in bem-sucedido")
    class SuccessfulCheckinTests {

        @Test
        @DisplayName("Deve realizar o check-in quando há vaga disponível")
        void shouldCheckIn_WhenSlotIsAvailable() {
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
        @DisplayName("Deve salvar o check-in corretamente")
        void shouldSaveCheckin_WhenCheckInIsSuccessful() {
            // Cenário em que o checkin é salvo
            when(slotRepository.findAvailableSlot()).thenReturn(Optional.of(availableSlot));
            when(checkinRepository.save(any(Checkin.class))).thenReturn(new Checkin(availableSlot, car));

            Checkin checkin = checkinService.checkIn(car);

            // Verifica se o método save foi chamado no repositório de checkin
            verify(checkinRepository, times(1)).save(any(Checkin.class));
            assertNotNull(checkin);
        }
    }

    @Nested
    @DisplayName("Testes para falha no check-in")
    class FailedCheckinTests {

        @Test
        @DisplayName("Deve lançar ParkingFullException quando não houver vaga disponível")
        void shouldThrowParkingFullException_WhenNoSlotsAvailable() {
            // Cenário em que não há vaga disponível
            when(slotRepository.findAvailableSlot()).thenReturn(Optional.empty());

            // Verifica se o ParkingFullException é lançado
            ParkingFullException thrown = assertThrows(ParkingFullException.class, () -> checkinService.checkIn(car));
            assertEquals("No available parking slots", thrown.getMessage());
        }

        @Test
        @DisplayName("Deve marcar a vaga como ocupada após check-in bem-sucedido")
        void shouldMarkSlotAsOccupied_WhenCheckInIsSuccessful() {
            // Cenário em que a vaga é marcada como ocupada após check-in
            when(slotRepository.findAvailableSlot()).thenReturn(Optional.of(availableSlot));
            when(checkinRepository.save(any(Checkin.class))).thenReturn(new Checkin(availableSlot, car));

            checkinService.checkIn(car);

            // Verifica se o método de ocupar a vaga foi chamado
            assertTrue(availableSlot.isOccupied());
        }
    }
}
