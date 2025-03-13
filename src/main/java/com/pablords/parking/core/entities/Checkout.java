package com.pablords.parking.core.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
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
  private final Clock clock;

  public Checkout(Checkin checkin) {
    this(checkin, Clock.systemDefaultZone()); // Mantém compatibilidade com o construtor existente
  }

  public Checkout(Checkin checkin, Clock clock) {
    this.checkin = checkin;
    this.clock = clock;
    this.checkOutTime = LocalDateTime.now(clock);
    this.checkin.setCheckOutTime(this.getCheckOutTime());
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

  public void setParkingFee(Double parkingFee) {
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
    log.info("Check-in time: {}, Checkout time: {}", checkInTime, this.getCheckOutTime());

    if (checkInTime == null) {
      throw new CheckinTimeMissingException(ErrorMessages.CHECKIN_TIME_IS_MISSING);
    }

    long seconds = java.time.Duration.between(checkInTime, this.getCheckOutTime()).getSeconds();
    log.info("Seconds: {}", seconds);
    long minutes = (long) Math.ceil(seconds / MINUTES_IN_HOUR);
    log.info("Minutes: {}", minutes);
    double ratePerMinute = MINUTE_RATE_IN_CENTS;
    log.info("Rate per minute: {}", ratePerMinute);
    Double totalFee = minutes * ratePerMinute;
    log.info("Total fee: {}", totalFee);
    BigDecimal totalDecimal = BigDecimal.valueOf(totalFee).setScale(2, RoundingMode.HALF_UP);
    log.info("Total fee rounded: {}", totalDecimal.doubleValue());

    this.setParkingFee(totalDecimal.doubleValue());
  }
}
