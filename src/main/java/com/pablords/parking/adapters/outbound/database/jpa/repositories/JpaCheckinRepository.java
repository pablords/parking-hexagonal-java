package com.pablords.parking.adapters.outbound.database.jpa.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;

@Repository
public interface JpaCheckinRepository extends JpaRepository<CheckinModel, UUID> {
    @Query("SELECT c FROM CheckinModel c WHERE c.car.plate = :plate AND c.checkOutTime = null ORDER BY c.checkInTime DESC")
    Optional<CheckinModel> findLatestByCarPlate(@Param("plate") String plate);
    Optional<CheckinModel> findByCarPlate(String plate);
}
