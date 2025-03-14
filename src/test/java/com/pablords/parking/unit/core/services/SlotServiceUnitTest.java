package com.pablords.parking.unit.core.services;

import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.exceptions.SlotOccupiedException;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.services.SlotService;

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

  @Nested
  @DisplayName("Testes para ocupar uma vaga")
  class OccupySlotTests {
    @Test
    @DisplayName("Deve ocupar uma vaga disponível com sucesso")
    void shouldOccupySlot_WhenSlotIsAvailable() {
      when(slotRepository.save(any(Slot.class))).thenReturn(availableSlot);

      Slot result = slotService.occupySlot(availableSlot);

      assertTrue(result.isOccupied());
      verify(slotRepository, times(1)).save(availableSlot);
    }

    @Test
    @DisplayName("Deve lançar SlotOccupiedException quando a vaga já está ocupada")
    void shouldThrowSlotOccupiedException_WhenSlotIsAlreadyOccupied() {
      SlotOccupiedException thrown = assertThrows(SlotOccupiedException.class,
          () -> slotService.occupySlot(occupiedSlot));
      assertEquals("This slot is already occupied", thrown.getMessage());
    }
  }

  @Nested
  @DisplayName("Testes para liberar uma vaga")
  class FreeSlotTests {
    @Test
    @DisplayName("Deve liberar uma vaga ocupada com sucesso")
    void shouldFreeSlot_WhenSlotIsOccupied() {
      when(slotRepository.save(any(Slot.class))).thenReturn(availableSlot);

      slotService.freeSlot(availableSlot);

      assertFalse(availableSlot.isOccupied());
      verify(slotRepository, times(1)).save(availableSlot);
    }
  }

  @Nested
  @DisplayName("Testes para encontrar uma vaga disponível")
  class FindAvailableSlotTests {
    @Test
    @DisplayName("Deve retornar uma vaga disponível quando existir")
    void shouldReturnAvailableSlot_WhenSlotExists() {
      when(slotRepository.findAvailableSlot()).thenReturn(Optional.of(availableSlot));

      Slot result = slotService.findAvailableSlot();

      assertNotNull(result);
      assertFalse(result.isOccupied());
    }

    @Test
    @DisplayName("Deve lançar ParkingFullException quando não há vagas disponíveis")
    void shouldThrowParkingFullException_WhenNoSlotsAreAvailable() {
      when(slotRepository.findAvailableSlot()).thenReturn(Optional.empty());

      ParkingFullException thrown = assertThrows(ParkingFullException.class, () -> slotService.findAvailableSlot());
      assertEquals("No available parking slots", thrown.getMessage());
    }
  }
}
