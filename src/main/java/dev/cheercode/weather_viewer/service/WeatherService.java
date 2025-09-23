package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.dto.LocationDto;
import dev.cheercode.weather_viewer.dto.WeatherDto;
import dev.cheercode.weather_viewer.model.Location;

import java.util.List;

public interface WeatherService {
    List<WeatherDto> getWeather(List<Location> locations, String lang);
    WeatherDto getWeather(Location location, String lang);
    List<LocationDto> findLocations(String city);
}
