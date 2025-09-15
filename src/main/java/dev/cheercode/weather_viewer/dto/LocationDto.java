package dev.cheercode.weather_viewer.dto;

import java.math.BigDecimal;
import java.util.Map;

public record LocationDto(
        String city, // название города
        String country, // код страны, например "RU", "GB", "CA"
        String state, // название региона, если есть (это значение может оказаться null)
        BigDecimal latitude, // широта
        BigDecimal longitude, // долгота
        Map<String, String> localNames // карта с локализованными названиями (она имеет только два значения и создаётся так: Map.of("en", Objects.toString(en, ""), "ru", Objects.toString(ru, "")) )
) {
}
