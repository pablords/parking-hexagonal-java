package com.pablords.parking.core.entities;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Checkin {
  private UUID id;
  private Slot slot;
  private Car car;
  private LocalDateTime checkInTime;
  private LocalDateTime checkOutTime;

  public Checkin(Slot slot, Car car) {
    this(slot, car, Clock.systemDefaultZone());
  }

  public Checkin(Slot slot, Car car, Clock clock) {
    this.slot = slot;
    this.car = car;
    this.checkInTime = LocalDateTime.now(clock);
  }

  public Checkin() {
    //TODO Auto-generated constructor stub
  }

  public LocalDateTime getCheckInTime() {
    return checkInTime;
  }

  public Slot getSlot() {
    return slot;
  }

  public Car getCar() {
    return car;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setCheckOutTime(LocalDateTime checkOutTime) {
    this.checkOutTime = checkOutTime;
  }

  public LocalDateTime getCheckOutTime() {
    return this.checkOutTime;
  }

  public void setCheckInTime(LocalDateTime minusHours) {
    this.checkInTime = minusHours;
  }

  public void setCar(Car car) {
    this.car = car;
  }

  public void setSlot(Slot slot) {
    this.slot = slot;
  }

  @Override
  public String toString() {
    return "Checkin [car=" + car + ", checkInTime=" + checkInTime + ", checkOutTime=" + checkOutTime + ", id=" + id
        + ", slot=" + slot.toString() + "]";
  }
}
