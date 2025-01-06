package com.pablords.parking.core.valueObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pablords.parking.core.exceptions.InvalidPlateException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class Plate {
    private String value;

    public static final Pattern VALID_PLATE_REGEX = Pattern.compile("[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}|[A-Z]{3}[0-9]{4}",
            Pattern.CASE_INSENSITIVE);

    Plate() {

    }

    public Plate(String value) {
        log.debug("Iniciando validação da placa: {}", value);
        this.value = value != null ? value : "";
        Matcher matcher = VALID_PLATE_REGEX.matcher(value);
        if (!matcher.find()) {
            throw new InvalidPlateException();
        }
    }

    public String getValue() {
        return value;
    }


}
