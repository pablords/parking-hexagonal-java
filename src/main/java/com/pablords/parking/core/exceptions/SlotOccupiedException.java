package com.pablords.parking.core.exceptions;

public class SlotOccupiedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SlotOccupiedException() {
        super("This slot is already occupied");
    }
}
