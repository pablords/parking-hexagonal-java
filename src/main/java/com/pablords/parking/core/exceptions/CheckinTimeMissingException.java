package com.pablords.parking.core.exceptions;

public class CheckinTimeMissingException extends RuntimeException {
    public CheckinTimeMissingException(String message) {
        super(message);
    }
}
