package com.pablords.parking.adapters.inbound.http.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pablords.parking.core.exceptions.CarNotFoundException;
import com.pablords.parking.core.exceptions.CheckinNotFoundException;
import com.pablords.parking.core.exceptions.CheckinTimeMissingException;
import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.exceptions.InvalidCheckinException;
import com.pablords.parking.core.exceptions.InvalidPlateException;
import com.pablords.parking.core.exceptions.ParkingFullException;
import com.pablords.parking.core.exceptions.SlotOcupiedException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();

    static {
        EXCEPTION_STATUS_MAP.put(CarNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(CheckinNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(CheckinTimeMissingException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(ExistPlateException.class, HttpStatus.CONFLICT);
        EXCEPTION_STATUS_MAP.put(InvalidCheckinException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(InvalidPlateException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(ParkingFullException.class, HttpStatus.SERVICE_UNAVAILABLE);
        EXCEPTION_STATUS_MAP.put(SlotOcupiedException.class, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error(ex.getMessage(), ex);

        HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);

        var error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status.value()).body(error);
    }
}
