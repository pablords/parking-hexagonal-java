package com.pablords.parking.core.entities;

import java.util.UUID;

public class Slot {
    private UUID id;
    private boolean occupied;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void free() {
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public String toString() {
        return "Slot [id=" + id + ", occupied=" + occupied + "]";
    }
}
