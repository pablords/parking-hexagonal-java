package com.pablords.parking.adapters.outbound.database.jpa.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "checkouts")
@Data
public class CheckoutModel {
    @Column(name = "id")
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "checkinId")
    private UUID checkinId;

    @Column(name = "checkOutTime")
    private LocalDateTime checkOutTime;

    @Column(name = "parkingFee")
    private Double parkingFee;
}
