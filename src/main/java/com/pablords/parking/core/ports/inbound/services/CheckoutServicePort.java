package com.pablords.parking.core.ports.inbound.services;

import com.pablords.parking.core.entities.Checkout;

public interface CheckoutServicePort {
    Checkout checkout(String plate);
}
