package com.pablords.parking.adapters.outbound.database.jpa.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "checkins")
@Data
@ToString
public class CheckinModel {
  @Column(name = "id")
  @Id
  @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "checkInTime")
  private LocalDateTime checkInTime;

  @ManyToOne()
  @JoinColumn(name = "car_id", referencedColumnName = "id") // Mapeamento da chave estrangeira
  private CarModel car;

  @ManyToOne()
  @JoinColumn(name = "slot_id", referencedColumnName = "id") // Mapeamento da chave estrangeira
  private SlotModel slot;

  @OneToOne(mappedBy = "checkin", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private CheckoutModel checkout;

}
