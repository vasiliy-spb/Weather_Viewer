package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.dto.LocationDto;
import dev.cheercode.weather_viewer.dto.WeatherDto;
import dev.cheercode.weather_viewer.exception.WeatherServiceException;
import dev.cheercode.weather_viewer.model.Location;
import dev.cheercode.weather_viewer.pojo.GeoData;
import dev.cheercode.weather_viewer.pojo.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
public class WeatherServiceByApi implements WeatherService {
    private final WebClient webClient;
    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherServiceByApi(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openweathermap.org").build();
    }

    private Mono<List<GeoData>> getGeoData(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/direct")
                        .queryParam("q", city)
                        .queryParam("limit", 5)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new WeatherServiceException("Weather API error")))
                .bodyToFlux(GeoData.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .collectList();
    }

    private Mono<List<GeoData>> getGeoData(BigDecimal lat, BigDecimal lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/reverse")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("limit", 5)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new WeatherServiceException("Weather API error")))
                .bodyToFlux(GeoData.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .collectList();
    }

    private Mono<WeatherData> getWeatherData(BigDecimal lat, BigDecimal lon, String lang) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("units", getUnitsByLang(lang))
                        .queryParam("lang", lang)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new WeatherServiceException("Weather API error")))
                .bodyToMono(WeatherData.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)));
    }

    public List<WeatherDto> getWeather(List<Location> locations, String lang) {
        return locations.stream()
                .map(location -> getWeather(location, lang))
                .toList();
    }

    public WeatherDto getWeather(Location location, String lang) {
        WeatherData weatherData = getWeatherData(location.getLatitude(), location.getLongitude(), lang).block();
        return mapToDto(location, weatherData);
    }

    private String getUnitsByLang(String lang) {
        return switch (lang) {
            case "ru" -> "metric";
            case "en" -> "imperial";
            default -> "standard";
        };
    }

    private WeatherDto mapToDto(Location location, WeatherData weatherData) {
        LocalTime currentTime = LocalTime.now();
        String currentTimeFormatted = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime sunrise = getSecondsSinceMidnight(weatherData.getSys().getSunrise(), weatherData.getTimezone());
        LocalTime sunset = getSecondsSinceMidnight(weatherData.getSys().getSunset(), weatherData.getTimezone());

        double sunPositionPercentage = calculateTimePosition(currentTime); // Положение солнца в процентах (0-100)
        double sunrisePositionPercentage = calculateTimePosition(sunrise); // положение рассвета в %
        double sunsetPositionPercentage = calculateTimePosition(sunset);   // положение заката в %

        boolean isDay = sunrisePositionPercentage <= sunPositionPercentage && sunPositionPercentage <= sunsetPositionPercentage;

        return new WeatherDto(
                location.getLatitude(),
                location.getLongitude(),
                weatherData.getName(),
                weatherData.getSys().getCountry(),
                weatherData.getMain().getTemp(),
                weatherData.getMain().getFeelsLike(),
                weatherData.getMain().getMinTemp(),
                weatherData.getMain().getMaxTemp(),
                weatherData.getMain().getHumidity(),
                weatherData.getMain().getPressure(),
                weatherData.getWeather()[0].getMain(),
                weatherData.getWeather()[0].getDescription(),
                weatherData.getWeather()[0].getIcon(),
                weatherData.getWind().getSpeed(),
                weatherData.getClouds().getAll(),
                currentTime,
                sunrise,
                sunset,
                sunrisePositionPercentage,
                currentTimeFormatted,
                sunrisePositionPercentage,
                sunsetPositionPercentage,
                isDay
        );
    }

    private double calculateTimePosition(LocalTime time) {
        return (time.getHour() * 60 + time.getMinute()) / (24.0 * 60) * 100;
    }

    public List<LocationDto> findLocations(String city) {
        List<GeoData> geoDatas = getGeoData(city).block();
        return geoDatas.stream()
                .map(this::mapToDto)
                .toList();
    }

    private LocalTime getSecondsSinceMidnight(long time, long timezone) {
        final long SECONDS_PER_DAY = 86400;
        long localTime = time + timezone;
        long seconds = Math.floorMod(localTime, SECONDS_PER_DAY);
        return LocalTime.ofSecondOfDay(seconds);
    }

    private LocationDto mapToDto(GeoData geoData) {
        return new LocationDto(
                geoData.getName(),
                geoData.getCountry(),
                geoData.getState(),
                geoData.getLat(),
                geoData.getLon(),
                geoData.getLocalNames() != null ? geoData.getLocalNames().asMap() : new HashMap<>()
        );
    }
}
