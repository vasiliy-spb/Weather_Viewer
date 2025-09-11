package dev.cheercode.weather_viewer.controller;

import dev.cheercode.weather_viewer.pojo.GeoData;
import dev.cheercode.weather_viewer.pojo.WeatherData;
import dev.cheercode.weather_viewer.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WeatherController {
    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/geo")
    public Mono<List<GeoData>> getGeoData(@RequestParam String city) {
        return weatherService.getGeoData(city);
    }

    @GetMapping("/weather")
    public Mono<WeatherData> getWeatherData(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon,
            @RequestParam(required = false, defaultValue = "en") String lang,
            @RequestParam(required = false) String units
    ) {
        if (units == null || units.isEmpty()) {
            units = setUnitsByLang(lang);
        }
        return weatherService.getWeatherData(lat, lon, lang, units);
    }

    private String setUnitsByLang(String lang) {
        return switch (lang) {
            case "ru" -> "metric";
            case "en" -> "imperial";
            default -> "standard";
        };
    }
}
