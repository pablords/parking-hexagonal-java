package com.pablords.parking.adapters.outbound.database.jpa.repositories.adapters;

import com.pablords.parking.adapters.outbound.database.jpa.mappers.CheckoutMapper;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.springdata.JpaCheckoutRepository;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckoutRepositoryAdapter implements CheckoutRepositoryPort {

    private final JpaCheckoutRepository jpaRepositoryCheckout;
    private final CheckoutMapper checkoutMapper;

    public CheckoutRepositoryAdapter(JpaCheckoutRepository jpaRepositoryCheckout, CheckoutMapper checkoutMapper) {
        this.jpaRepositoryCheckout = jpaRepositoryCheckout;
        this.checkoutMapper = checkoutMapper;
    }

    @Override
    public Checkout save(Checkout checkout) {
        log.info("Persistindo checkout: {}", checkout.toString());
        var createdCheckout = jpaRepositoryCheckout.save(checkoutMapper.toModel(checkout));
        return checkoutMapper.toEntity(createdCheckout);
    }

    @Override
    public Optional<Checkout> findByCheckinId(UUID checkinId) {
        var optionalCheckoutModel = jpaRepositoryCheckout.findByCheckinId(checkinId);
        
        if (optionalCheckoutModel.isEmpty()) {
            return Optional.empty();
        }

        var checkin = new Checkin();
        checkin.setId(checkin.getId());
        return Optional.of(checkoutMapper.toEntity(optionalCheckoutModel.get()));
    }
}
