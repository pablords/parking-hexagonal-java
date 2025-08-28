package com.pablords.parking.core.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import java.time.*;

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


  /**
   * Atualiza a taxa de estacionamento calculando novamente o valor.
   * Método público para uso externo controlado.
   */
  public void updateParkingFee() {
    this.calculateParkingFee();
  }

  public Checkout(Checkin checkin) {
    this(checkin, Clock.systemDefaultZone()); // Mantém compatibilidade com o construtor existente
    this.calculateParkingFee();
  }

  public Checkout(Checkin checkin, Clock clock) {
    this.checkin = checkin;
    this.clock = clock;
    this.checkOutTime = LocalDateTime.now(clock);
    this.checkin.setCheckOutTime(this.getCheckOutTime());
    this.calculateParkingFee();
  }

  public Checkout() {
    this.clock = Clock.systemDefaultZone();
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

  private void calculateParkingFee() {
    final double HOURLY_RATE_IN_CENTS = 2.50;
    final double MINUTES_IN_HOUR = 60;
    final double MINUTE_RATE_IN_CENTS = HOURLY_RATE_IN_CENTS / MINUTES_IN_HOUR;

    LocalDateTime checkInTime = checkin.getCheckInTime();
    log.info("Check-in time: {}, Checkout time: {}", checkInTime, this.getCheckOutTime());

    if (checkInTime == null) {
      throw new CheckinTimeMissingException(ErrorMessages.CHECKIN_TIME_IS_MISSING);
    }

    long seconds = Duration.between(checkInTime, this.getCheckOutTime()).getSeconds();
    log.info("Seconds: {}", seconds);
    long minutes = (long) Math.ceil(seconds / MINUTES_IN_HOUR);
    log.info("Minutes: {}", minutes);
    double totalFee = minutes * MINUTE_RATE_IN_CENTS;
    log.info("Total fee: {}", totalFee);
    BigDecimal totalDecimal = BigDecimal.valueOf(totalFee).setScale(2, RoundingMode.HALF_UP);
    log.info("Total fee rounded: {}", totalDecimal.doubleValue());

    this.setParkingFee(totalDecimal.doubleValue());
  }
}
