package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.CheckinMapper;
import com.pablords.parking.adapters.outbound.database.jpa.mappers.CheckoutMapper;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class CheckoutRepositoryAdapter implements CheckoutRepositoryPort {

    private final JpaRepositoryCheckout jpaRepositoryCheckout;

    public CheckoutRepositoryAdapter(JpaRepositoryCheckout jpaRepositoryCheckout) {
        this.jpaRepositoryCheckout = jpaRepositoryCheckout;
    }

    @Override
    public Checkout save(Checkout checkout) {
        var createdCheckout = jpaRepositoryCheckout.save(CheckoutMapper.toModel(checkout));
        return CheckoutMapper.toEntity(createdCheckout, checkout.getCheckin());
    }

    @Override
    public Optional<Checkout> findByCheckinId(Long checkinId) {
        var optionalCheckoutModel = jpaRepositoryCheckout.findByCheckinId(checkinId);
        
        // Caso o checkout não seja encontrado, retorna Optional.empty()
        if (optionalCheckoutModel.isEmpty()) {
            System.out.println("Checkout não encontrado para o checkinId: " + checkinId);
            return Optional.empty();
        }

        var checkin = new Checkin();
        checkin.setId(checkin.getId());
        return Optional.of(CheckoutMapper.toEntity(optionalCheckoutModel.get(), checkin));
    }
}
