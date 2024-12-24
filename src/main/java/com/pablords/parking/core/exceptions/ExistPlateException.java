package com.pablords.parking.core.exceptions;

public class ExistPlateException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExistPlateException(String message) {
        super(message);
    }

    public ExistPlateException() {
        super("Plate Exist");
    }
}
