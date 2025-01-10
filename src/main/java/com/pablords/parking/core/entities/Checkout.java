package com.pablords.parking.core.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.exceptions.ErrorMessages;

public class Checkout {
    private UUID id;
    private Checkin checkin;
    private LocalDateTime checkOutTime;
    private Double parkingFee;

    public Checkout(Checkin checkin) {
        this.checkin = checkin;
        this.checkOutTime = LocalDateTime.now();
    }

    public void freeSlot() {
        checkin.getSlot().free();
    }

    public Double getParkingFee() {
        return parkingFee;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    private void setParkingFee(Double parkingFee) {
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

    public void calculateParkingFee() {
        final double HOURLY_RATE_IN_CENTS = 2.50;
        final double MINUTES_IN_HOUR = 60;
        final double MINUTE_RATE_IN_CENTS = HOURLY_RATE_IN_CENTS / MINUTES_IN_HOUR;

        LocalDateTime checkInTime = checkin.getCheckInTime();
        if (checkInTime == null) {
            throw new CheckinTimeMissingException(ErrorMessages.CHECKIN_TIME_IS_MISSING);
        }

        long seconds = java.time.Duration.between(checkInTime, LocalDateTime.now()).getSeconds();
        long minutes = (long) Math.ceil(seconds / MINUTES_IN_HOUR);
        double ratePerMinute = MINUTE_RATE_IN_CENTS;
        Double totalFee = minutes * ratePerMinute;
        BigDecimal totalDecimal = BigDecimal.valueOf(totalFee).setScale(2, RoundingMode.HALF_UP); // Arredonda para cima se >= 0.5

        this.setParkingFee(totalDecimal.doubleValue());
    }
}
