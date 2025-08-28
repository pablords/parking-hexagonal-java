package com.pablords.parking.adapters.inbound.http.handlers;
import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiErrorFactory {
  public static ApiErrorDTO createApiError(HttpStatus status, String message, String path, Clock clock) {
      return ApiErrorDTO.builder()
          .timestamp(LocalDateTime.now(clock))
          .error(status.getReasonPhrase())
          .message(message)
          .path(path)
          .build();
  }
}