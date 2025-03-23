package com.pablords.parking.adapters.inbound.http.handlers;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String DEFAULT_MESSAGE_SERVER_ERROR = "Contact Admin Server";
  private final Clock clock;

  public GlobalExceptionHandler() {
    this(Clock.systemDefaultZone());
  }

  public GlobalExceptionHandler(Clock clock) {
    this.clock = clock;
  }

  @ExceptionHandler({ RuntimeException.class })
  public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
    log.error(ex.getMessage(), ex);

    HttpStatus status = ExceptionStatusMapper.getStatus(ex.getClass());
    var error = ApiErrorFactory.createApiError(
        status,
        HttpStatus.valueOf(status.value()).value() == 500 ? DEFAULT_MESSAGE_SERVER_ERROR : ex.getMessage(),
        request.getRequestURI(),
        clock
    );
    return ResponseEntity.status(status.value()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, String> errors = extractValidationErrors(ex);
    var error = ApiErrorFactory.createApiError(
        HttpStatus.UNPROCESSABLE_ENTITY,
        "Validation error",
        request.getRequestURI(),
        clock
    );
    error.setErrors(errors);
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
  }

  private Map<String, String> extractValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });
    return errors;
  }
}
