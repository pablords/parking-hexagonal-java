package com.pablords.parking.adapters.outbound.database.jpa.repositories.springdata;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;

@Repository
public interface JpaCheckoutRepository extends JpaRepository<CheckoutModel, UUID> {
    Optional<CheckoutModel> findByCheckinId(UUID checkinId);
}
