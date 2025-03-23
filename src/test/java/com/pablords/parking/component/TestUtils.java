package com.pablords.parking.component;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaSlotRepository;

public class TestUtils {
  private final static LocalDateTime NOW = LocalDateTime.now();

  public static void mockSlotRepository(JpaSlotRepository jpaSlotRepositoryMock, ArrayList<SlotModel> slots) {
    // Mock para buscar o primeiro slot disponível
    when(jpaSlotRepositoryMock.findFirstByOccupiedFalse()).thenReturn(Optional.of(slots.get(0)));

    // Mock para buscar um slot pelo ID
    when(jpaSlotRepositoryMock.findById(any())).thenAnswer(invocation -> {
      Long id = invocation.getArgument(0);
      return slots.stream().filter(slot -> slot.getId().equals(id)).findFirst();
    });

    // Mock para salvar um slot e atualizar a ocupação corretamente
    when(jpaSlotRepositoryMock.save(any(SlotModel.class))).thenAnswer(invocation -> {
      SlotModel slot = invocation.getArgument(0);
      slots.stream().filter(s -> s.getId().equals(slot.getId())).findFirst()
          .ifPresent(s -> s.setOccupied(slot.isOccupied()));
      return slot;
    });
  }

  public static CheckinModel createCheckinModel(CarModel car, ArrayList<SlotModel> slots) {
    CheckinModel createdCheckin = new CheckinModel();
    createdCheckin.setCar(car);
    createdCheckin.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
    createdCheckin.setCheckInTime(NOW);
    createdCheckin.setSlot(slots.get(0));
    return createdCheckin;
  }

  public static CarModel createCarModel() {
    CarModel car = new CarModel();
    car.setPlate("ABC1234");
    car.setBrand("Audi");
    car.setColor("Black");
    car.setModel("A4");
    car.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
    car.setCreatedAt(NOW);
    car.setUpdatedAt(NOW);
    return car;
  }

  public static ArrayList<SlotModel> createSlots() {
    ArrayList<SlotModel> slots = new ArrayList<>();
    for (int i = 1; i <= 2; i++) {
      SlotModel slot = new SlotModel();
      slot.setId((long) i);
      slot.setOccupied(false);
      slots.add(slot);
    }
    return slots;
  }

  public static CheckoutModel createCheckoutModel(CheckinModel createdCheckin) {
    CheckoutModel checkout = new CheckoutModel();
    checkout.setCheckinId(createdCheckin.getId());
    checkout.setCheckOutTime(NOW.plusHours(2));
    checkout.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
    return checkout;
  }
}