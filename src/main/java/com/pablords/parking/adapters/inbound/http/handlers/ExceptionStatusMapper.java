package com.pablords.parking.adapters.inbound.http.handlers;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.pablords.parking.core.exceptions.CarNotFoundException;
import com.pablords.parking.core.exceptions.CheckinNotFoundException;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.exceptions.InvalidCheckinException;
import com.pablords.parking.core.exceptions.InvalidPlateException;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.exceptions.SlotOccupiedException;

public class ExceptionStatusMapper {

  private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
      CarNotFoundException.class, HttpStatus.NOT_FOUND,
      CheckinNotFoundException.class, HttpStatus.NOT_FOUND,
      CheckinTimeMissingException.class, HttpStatus.BAD_REQUEST,
      ExistPlateException.class, HttpStatus.CONFLICT,
      InvalidCheckinException.class, HttpStatus.BAD_REQUEST,
      InvalidPlateException.class, HttpStatus.BAD_REQUEST,
      ParkingFullException.class, HttpStatus.SERVICE_UNAVAILABLE,
      SlotOccupiedException.class, HttpStatus.CONFLICT);

  public static HttpStatus getStatus(Class<? extends RuntimeException> exceptionClass) {
    return EXCEPTION_STATUS_MAP.getOrDefault(exceptionClass, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}