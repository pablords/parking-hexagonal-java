package com.pablords.parking.adapters.outbound.database.jpa.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "checkins")
@Data
public class CheckinModel {
    @Column(name = "id")
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "slotId")
    private UUID slotId;

    @Column(name = "carPlate")
    private String carPlate;

    @Column(name = "checkInTime")
    private LocalDateTime checkInTime;

    @Column(name = "checkOutTime")
    private LocalDateTime checkOutTime;
}
