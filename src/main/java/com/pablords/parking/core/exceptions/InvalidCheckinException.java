package com.pablords.parking.core.exceptions;

public class InvalidCheckinException extends RuntimeException {
    public InvalidCheckinException(String message) {
        super(message);
    }
}
