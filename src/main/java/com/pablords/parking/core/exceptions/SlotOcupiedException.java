package com.pablords.parking.core.exceptions;

public class SlotOcupiedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SlotOcupiedException() {
        super("Slot Ocupied");
    }
}
