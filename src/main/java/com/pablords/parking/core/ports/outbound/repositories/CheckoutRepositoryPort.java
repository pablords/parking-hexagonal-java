package com.pablords.parking.core.ports.outbound.repositories;

import java.util.Optional;

import com.pablords.parking.core.entities.Checkout;

public interface CheckoutRepositoryPort {
    Checkout save(Checkout checkout);
    Optional<Checkout> findByCheckinId(Long checkinId);
}
