package com.pablords.parking.core.entities;

public class Slot {
    private Long id;
    private boolean occupied;
    private Checkin checkin;
    
    public Checkin getCheckin() {
        return checkin;
    }

    public void setCheckin(Checkin checkin) {
        this.checkin = checkin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
