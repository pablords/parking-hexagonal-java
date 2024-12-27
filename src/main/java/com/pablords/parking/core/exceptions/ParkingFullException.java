package com.pablords.parking.core.exceptions;

public class ParkingFullException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ParkingFullException() {
        super("No available parking slots");
    }
}
