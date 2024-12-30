package com.pablords.parking.core.entities;

import java.time.LocalDateTime;

public class Checkout {
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Checkin getCheckin() {
        return checkin;
    }

    public void setCheckin(Checkin checkin) {
        this.checkin = checkin;
    }
}
