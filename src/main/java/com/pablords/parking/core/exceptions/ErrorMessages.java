package com.pablords.parking.core.exceptions;

public class ErrorMessages {
    public static final String CAR_WITH_PLATE_EXISTS = "A car with this plate already exists: %s";
    public static final String CAR_WITH_PLATE_NOT_EXISTS = "A car with this plate Not exists: %s";
    public static final String INVALID_CHECKIN_CHECKOUT_NOT_FOUND = "Invalid checkin, checkin is open";
    public static final String CHECKIN_NOT_FOUND_BY_PLATE = "No check-in found for plate: %s";
    public static final String CHECKIN_NOT_FOUND_BY_ID = "No check-in found for id: %s";
    public static final String CAR_NOT_FOUND_BY_PLATE = "No car found for plate: %s";
    public static final String CHECKIN_TIME_IS_MISSING = "Checkin time is missing";
}
