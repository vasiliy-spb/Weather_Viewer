package dev.cheercode.weather_viewer.model;

import java.math.BigDecimal;

public record Location(
        Long id,
        String name,
        Long userId,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
