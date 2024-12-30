package com.pablords.parking.adapters.outbound.database.jpa.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "checkouts")
@Data
public class CheckoutModel {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checkinId")
    private Long checkinId;

    @Column(name = "checkOutTime")
    private LocalDateTime checkOutTime;

    @Column(name = "parkingFee")
    private long parkingFee;
}
