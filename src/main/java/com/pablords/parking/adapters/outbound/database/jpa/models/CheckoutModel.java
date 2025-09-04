package com.pablords.parking.adapters.outbound.database.jpa.models;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "checkouts")
@Data
public class CheckoutModel {
  @Column(name = "id")
  @Id
  @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "checkin_id", referencedColumnName = "id")
  private CheckinModel checkin;

  @Column(name = "checkOutTime")
  private LocalDateTime checkOutTime;

  @Column(name = "parkingFee")
  private Double parkingFee;
}
