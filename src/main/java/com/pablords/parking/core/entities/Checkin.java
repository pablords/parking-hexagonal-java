package com.pablords.parking.core.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class Checkin {
    private UUID id;
    private Slot slot;
    private Car car;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public Checkin() {
        this.checkInTime = LocalDateTime.now();
    }

    public Checkin(Slot slot, Car car) {
        this.slot = slot;
        this.car = car;
        this.checkInTime = LocalDateTime.now();
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
