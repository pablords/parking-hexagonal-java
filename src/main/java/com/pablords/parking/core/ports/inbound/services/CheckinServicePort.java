package com.pablords.parking.core.ports.inbound.services;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;

public interface CheckinServicePort {
    Checkin checkIn(Car car);
}
