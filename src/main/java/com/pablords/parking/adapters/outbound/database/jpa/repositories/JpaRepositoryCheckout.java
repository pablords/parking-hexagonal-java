package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;

@Repository
public interface JpaRepositoryCheckout extends JpaRepository<CheckoutModel, Long> {
    
}
