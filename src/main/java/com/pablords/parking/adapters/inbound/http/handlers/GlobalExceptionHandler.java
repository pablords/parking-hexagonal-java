package com.pablords.parking.adapters.inbound.http.handlers;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pablords.parking.core.exceptions.CarNotFoundException;
import com.pablords.parking.core.exceptions.CheckinNotFoundException;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.exceptions.InvalidCheckinException;
import com.pablords.parking.core.exceptions.InvalidPlateException;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.exceptions.SlotOccupiedException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();
  private static final String DEFAULT_MESSAGE_SERVER_ERROR = "Contact Admin Server";

  private final Clock clock;

  public GlobalExceptionHandler() {
    this(Clock.systemDefaultZone());
  }

  public GlobalExceptionHandler(Clock clock) {
    this.clock = clock;
  }

  static {
    EXCEPTION_STATUS_MAP.put(CarNotFoundException.class, HttpStatus.NOT_FOUND);
    EXCEPTION_STATUS_MAP.put(CheckinNotFoundException.class, HttpStatus.NOT_FOUND);
    EXCEPTION_STATUS_MAP.put(CheckinTimeMissingException.class, HttpStatus.BAD_REQUEST);
    EXCEPTION_STATUS_MAP.put(ExistPlateException.class, HttpStatus.CONFLICT);
    EXCEPTION_STATUS_MAP.put(InvalidCheckinException.class, HttpStatus.BAD_REQUEST);
    EXCEPTION_STATUS_MAP.put(InvalidPlateException.class, HttpStatus.BAD_REQUEST);
    EXCEPTION_STATUS_MAP.put(ParkingFullException.class, HttpStatus.SERVICE_UNAVAILABLE);
    EXCEPTION_STATUS_MAP.put(SlotOccupiedException.class, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({ RuntimeException.class })
  public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex,
      HttpServletRequest request) {
    log.error(ex.getMessage(), ex);

    HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(),
        HttpStatus.INTERNAL_SERVER_ERROR);

    var error = ApiError.builder()
        .timestamp(LocalDateTime.now(clock))
        .error(status.getReasonPhrase())
        .message(HttpStatus.valueOf(status.value()).value() == 500 ? DEFAULT_MESSAGE_SERVER_ERROR
            : ex.getMessage())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(status.value()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });
    var error = ApiError.builder()
        .timestamp(LocalDateTime.now(clock))
        .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
        .errors(errors)
        .message("Validation error")
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
  }

}
