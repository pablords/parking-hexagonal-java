package com.pablords.parking.core.exceptions;

public class InvalidPlateException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidPlateException() {
        super("Invalid plate");
    }
}
