package com.hackaboss.travelagency.util;

public class RoomTypeConverter {

    private RoomTypeConverter() {
    }

    public static RoomType fromString(String value) {
        try {
            return RoomType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de habitación inválido: '" + value + "'. Valores permitidos: INDIVIDUAL, DOBLE, TRIPLE, SUITE");
        }
    }
}
