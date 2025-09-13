package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.model.Location;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.pojo.GeoData;
import dev.cheercode.weather_viewer.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public List<Location> getUsersLocations(User user) {
        return locationRepository.findByUser(user);
    }

    public Location addToUser(User user, GeoData geoData) {
        Location location = new Location();
        location.setUser(user);
        location.setName(geoData.getName());
        location.setLatitude(geoData.getLat());
        location.setLongitude(geoData.getLon());

        return locationRepository.save(location);
    }

    public void deleteUserLocation(User user, Long locationId) {
        locationRepository.deleteByIdAndUser(locationId, user);
    }
}
