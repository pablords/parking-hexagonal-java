package com.pablords.parking.adapters.inbound.api.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.exceptions.InvalidPlateException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ InvalidPlateException.class })
    public ResponseEntity<String> wrongPlate(RuntimeException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ExistPlateException.class })
    public ResponseEntity<String> existPlate(RuntimeException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
