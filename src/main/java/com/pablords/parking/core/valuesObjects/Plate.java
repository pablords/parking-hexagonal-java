package com.pablords.parking.core.valuesObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pablords.parking.core.exceptions.InvalidPlateException;

public class Plate {
    private String value;

    public static final Pattern VALID_PLATE_REGEX = Pattern.compile("[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}|[A-Z]{3}[0-9]{4}",
            Pattern.CASE_INSENSITIVE);

    Plate() {

    }

    Plate(String value) {
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
