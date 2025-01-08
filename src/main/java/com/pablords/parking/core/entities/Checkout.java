package com.pablords.parking.core.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.UUID;

import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.exceptions.ErrorMessages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        checkin.getSlot().free(); // Libera a vaga
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

        LocalDateTime checkInTime = checkin.getCheckInTime();
        if (checkInTime == null) {
            throw new CheckinTimeMissingException(ErrorMessages.CHECKIN_TIME_IS_MISSING);
        }

        // Calcula a duração total em segundos
        long seconds = java.time.Duration.between(checkInTime, LocalDateTime.now()).getSeconds();

        // Converte segundos para minutos (arredondando para cima, garantindo cobrança
        // mínima de 1 minuto)
        long minutes = (long) Math.ceil(seconds / 60.0);

        // Converte a taxa horária para minutos (2,50 por 60 minutos)
        double ratePerMinute = HOURLY_RATE_IN_CENTS / 60;

        // Calcula a taxa total
        Double totalFee = minutes * ratePerMinute;
        BigDecimal bd = BigDecimal.valueOf(totalFee).setScale(2, RoundingMode.HALF_UP); // Arredonda para cima se >= 0.5


        log.info(String.format(
                "Check-in: %s, Total segundos: %d, Total minutos: %d, Média por minuto: %.2f, Taxa total em centavos: %.2f",
                checkInTime, seconds, minutes, ratePerMinute, totalFee));

        this.setParkingFee(bd.doubleValue());
    }
}
