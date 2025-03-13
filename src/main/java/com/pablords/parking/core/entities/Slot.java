package com.pablords.parking.core.entities;

public class Slot {
  private Long id;
  private boolean occupied;

  public void setOccupied(boolean occupied) {
    this.occupied = occupied;
  }

  public Slot() {
  }

  public Slot(Long id, boolean occupied) {
    this.id = id;
    this.occupied = occupied;
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
