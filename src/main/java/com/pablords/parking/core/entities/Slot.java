package com.pablords.parking.core.entities;

public class Slot {
    private Long id;
    private boolean occupied;


    public void occupy() {
        this.occupied = true;
    }

    public void free() {
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }
}
