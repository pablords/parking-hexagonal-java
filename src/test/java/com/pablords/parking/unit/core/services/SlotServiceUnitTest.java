package com.pablords.parking.unit.core.services;

import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.exceptions.SlotOccupiedException;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.services.SlotService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlotServiceUnitTest {

    @Mock
    private SlotRepositoryPort slotRepository;

    @InjectMocks
    private SlotService slotService;

    private Slot availableSlot;
    private Slot occupiedSlot;

    @BeforeEach
    void setUp() {
        availableSlot = new Slot();
        availableSlot.free();

        occupiedSlot = new Slot();
        occupiedSlot.occupy();
    }

    @Test
    void testOccupySlot_Success() {
        // Cenário: A vaga não está ocupada e deve ser ocupada com sucesso
        when(slotRepository.save(any(Slot.class))).thenReturn(availableSlot);

        Slot result = slotService.occupySlot(availableSlot);

        assertTrue(result.isOccupied());
        verify(slotRepository, times(1)).save(availableSlot);  // Verifica se o método save foi chamado uma vez
    }

    @Test
    void testOccupySlot_ThrowsSlotOccupiedException() {
        // Cenário: A vaga já está ocupada e deve lançar uma exceção
        SlotOccupiedException thrown = assertThrows(SlotOccupiedException.class, () -> slotService.occupySlot(occupiedSlot));
        assertEquals("This slot is already occupied", thrown.getMessage());  // Verifica a mensagem da exceção
    }

    @Test
    void testFreeSlot_Success() {
        // Cenário: A vaga foi ocupada e depois é liberada com sucesso
        when(slotRepository.save(any(Slot.class))).thenReturn(availableSlot);

        slotService.freeSlot(availableSlot);

        assertFalse(availableSlot.isOccupied());  // Verifica se a vaga foi liberada
        verify(slotRepository, times(1)).save(availableSlot);  // Verifica se o método save foi chamado
    }


    @Test
    void testFindAvailableSlot_Success() {
        // Cenário: Deve retornar uma vaga disponível
        when(slotRepository.findAvailableSlot()).thenReturn(Optional.of(availableSlot));

        Slot result = slotService.findAvailableSlot();

        assertNotNull(result);
        assertFalse(result.isOccupied());
    }

    @Test
    void testFindAvailableSlot_ThrowsParkingFullException() {
        // Cenário: Quando não houver vagas disponíveis, deve lançar uma exceção
        when(slotRepository.findAvailableSlot()).thenReturn(Optional.empty());

        ParkingFullException thrown = assertThrows(ParkingFullException.class, () -> slotService.findAvailableSlot());
        assertEquals("No available parking slots", thrown.getMessage());
    }
}
