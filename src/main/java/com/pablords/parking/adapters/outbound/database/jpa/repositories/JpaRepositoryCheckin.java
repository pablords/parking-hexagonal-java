package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;

@Repository
public interface JpaRepositoryCheckin extends JpaRepository<CheckinModel, Long> {
    @Query("SELECT c FROM CheckinModel c WHERE c.carPlate = :plate AND c.checkOutTime = null ORDER BY c.checkInTime DESC")
    Optional<CheckinModel> findLatestByCarPlate(@Param("plate") String plate);
    Optional<CheckinModel> findByCarPlate(String plate);
}
