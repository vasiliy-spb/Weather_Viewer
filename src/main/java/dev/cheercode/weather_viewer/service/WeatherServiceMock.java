package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.dto.LocationDto;
import dev.cheercode.weather_viewer.dto.WeatherDto;
import dev.cheercode.weather_viewer.model.Location;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class WeatherServiceMock implements WeatherService {
    @Override
    public List<WeatherDto> getWeather(List<Location> locations, String lang) {
        return locations.stream()
                .map(location -> getWeather(location, lang))
                .toList();
    }

    @Override
    public WeatherDto getWeather(Location location, String lang) {
        Random random = new Random();
        LocalTime now = LocalTime.of(random.nextInt(24), random.nextInt(60));
        LocalTime sunrise = LocalTime.of(7, 19);
        LocalTime sunset = LocalTime.of(17, 23);

        // Вычисляем продолжительность дня
        long daylightMinutes = java.time.Duration.between(sunrise, sunset).toMinutes();
        String daylightDuration = String.format("%02d:%02d", daylightMinutes / 60, daylightMinutes % 60);

        // Вычисляем положение солнца в процентах
        double sunrisePos = calculateTimePosition(sunrise);
        double sunsetPos = calculateTimePosition(sunset);
        double sunPos = calculateTimePosition(now);

        boolean isDay = !now.isBefore(sunrise) && now.isBefore(sunset);

        return new WeatherDto(
                location.getLatitude(),
                location.getLongitude(),
                location.getName(),
                "US",
                new BigDecimal("20"),
                new BigDecimal("18"),
                new BigDecimal("25"),
                new BigDecimal("15"),
                80,
                756,
                "Clear",
                "almost clear sky",
                "02d",
                new BigDecimal("2"),
                12,
                now,
                sunrise,
                sunset,
                sunPos,
                String.format("%02d:%02d", now.getHour(), now.getMinute()),
                sunrisePos,
                sunsetPos,
                isDay
        );
    }

    // Вычисляет положение времени на 24-часовой шкале в процентах
    private double calculateTimePosition(LocalTime time) {
        return (time.getHour() * 60 + time.getMinute()) / (24.0 * 60) * 100;
    }

    private double calculateSunPosition(LocalTime current, LocalTime sunrise, LocalTime sunset) {
        long totalDayLength = java.time.Duration.between(sunrise, sunset).toMinutes();
        long minutesSinceSunrise = java.time.Duration.between(sunrise, current).toMinutes();

        if (minutesSinceSunrise < 0) return 0; // До рассвета
        if (minutesSinceSunrise > totalDayLength) return 100; // После заката

        return (double) minutesSinceSunrise / totalDayLength * 100;
    }

    @Override
    public List<LocationDto> findLocations(String city) {
        List<LocationDto> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            result.add(getLocationDto(city));
        }
        return result;
    }

    private LocationDto getLocationDto(String city) {
        return new LocationDto(
                city,
                "US",
                "state name",
                new BigDecimal("23.123456"),
                new BigDecimal("12.123467"),
                Map.of("en", "city name", "ru", "название города")
        );
    }
}
