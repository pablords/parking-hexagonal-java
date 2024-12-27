package com.pablords.parking.core.ports.outbound.repositories;

import com.pablords.parking.core.entities.Checkout;

public interface CheckoutRepositoryPort {
    Checkout save(Checkout checkout);
}
