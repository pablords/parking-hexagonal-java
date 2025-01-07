package com.pablords.parking.core.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class Checkout {
    private UUID id;
    private Checkin checkin;
    private LocalDateTime checkOutTime;
    private long parkingFee;

    public Checkout(Checkin checkin) {
        this.checkin = checkin;
        this.checkOutTime = LocalDateTime.now();
    }

    public void freeSlot() {
        checkin.getSlot().free(); // Libera a vaga
    }

    public long getParkingFee() {
        return parkingFee;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setParkingFee(long parkingFee) {
        this.parkingFee = parkingFee;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Checkin getCheckin() {
        return checkin;
    }

    public void setCheckin(Checkin checkin) {
        this.checkin = checkin;
    }

    @Override
    public String toString() {
        return "Checkout{" +
                "id=" + id +
                ", checkin=" + checkin +
                ", checkOutTime=" + checkOutTime +
                ", parkingFee=" + parkingFee +
                '}';
    }
}
