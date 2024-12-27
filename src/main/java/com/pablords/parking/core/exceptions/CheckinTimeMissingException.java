package com.pablords.parking.core.exceptions;

public class CheckinTimeMissingException extends RuntimeException {
    public CheckinTimeMissingException() {
        super("Checkin time is missing");
    }
}
