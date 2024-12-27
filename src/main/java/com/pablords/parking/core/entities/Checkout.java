package com.pablords.parking.core.entities;

import java.time.LocalDateTime;

public class Checkout {
    private Checkin checkin;
    private LocalDateTime checkOutTime;
    private long parkingFee;

    public Checkout(Checkin checkin) {
        this.checkin = checkin;
        this.checkOutTime = LocalDateTime.now();
    }

    public void freeSlot() {
        checkin.getSlot().free();  // Libera a vaga
    }

    public long getParkingFee() {
        return parkingFee;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setParkingFee(long parkingFee){
        this.parkingFee = parkingFee;
    }
}
