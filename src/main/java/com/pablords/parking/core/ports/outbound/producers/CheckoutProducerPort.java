package com.pablords.parking.core.ports.outbound.producers;

import com.pablords.parking.core.entities.Checkout;

public interface CheckoutProducerPort {
    void sendCheckoutMessage(Checkout checkout);
}
