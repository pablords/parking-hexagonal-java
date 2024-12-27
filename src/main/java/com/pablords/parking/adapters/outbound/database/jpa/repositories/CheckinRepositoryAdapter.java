package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CheckinRepositoryAdapter implements CheckinRepositoryPort {

    private final JpaRepositoryCheckin jpaRepositoryCheckin;
    
    public CheckinRepositoryAdapter(JpaRepositoryCheckin jpaRepositoryCheckin){
        this.jpaRepositoryCheckin = jpaRepositoryCheckin;
    }

    @Override
    public Checkin save(Checkin checkin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Optional<Checkin> findByCarId(Long carId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByCarId'");
    }
 
}
