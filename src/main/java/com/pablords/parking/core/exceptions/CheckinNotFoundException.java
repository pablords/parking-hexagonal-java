package com.pablords.parking.core.exceptions;

public class CheckinNotFoundException extends RuntimeException {
    public CheckinNotFoundException(String message){
        super(message);
    }
}
