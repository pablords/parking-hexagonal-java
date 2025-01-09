package com.pablords.parking.core.valueobjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pablords.parking.core.exceptions.InvalidPlateException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Plate {
    private String value;

    // Adicionando suporte a placas com '-'
    public static final Pattern VALID_PLATE_REGEX = Pattern.compile(
            "([A-Z]{3}-\\d[A-Z]\\d{2})|([A-Z]{3}-\\d{4})|([A-Z]{3}\\d[A-Z]\\d{2})|([A-Z]{3}\\d{4})",
            Pattern.CASE_INSENSITIVE
    );

    Plate() {
    }

    public Plate(String value) {
        log.debug("Iniciando validação da placa: {}", value);
        this.value = value != null ? value.trim() : "";
        
        Matcher matcher = VALID_PLATE_REGEX.matcher(this.value);
        if (!matcher.matches()) { // Usando matches para validar a string inteira
            throw new InvalidPlateException();
        }
        log.debug("Placa valida: {}", value);
    }

    public String getValue() {
        return value;
    }
}
