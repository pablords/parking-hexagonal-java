package com.pablords.parking.core.utils;

public class StringUtils {
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false; // Se uma das Strings for null, retorna false
        }
        return str1.equalsIgnoreCase(str2);
    }
}
