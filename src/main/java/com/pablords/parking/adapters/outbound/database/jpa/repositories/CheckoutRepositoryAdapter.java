package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class CheckoutRepositoryAdapter implements CheckoutRepositoryPort {

    private final JpaRepositoryCheckout jpaRepositoryCheckout;

    public CheckoutRepositoryAdapter(JpaRepositoryCheckout jpaRepositoryCheckout) {
        this.jpaRepositoryCheckout = jpaRepositoryCheckout;
    }

    @Override
    public Checkout save(Checkout checkout) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
