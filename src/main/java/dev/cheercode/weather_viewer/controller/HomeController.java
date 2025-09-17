package dev.cheercode.weather_viewer.controller;

import dev.cheercode.weather_viewer.dto.LocationDto;
import dev.cheercode.weather_viewer.dto.WeatherDto;
import dev.cheercode.weather_viewer.exception.UnknownAttributeException;
import dev.cheercode.weather_viewer.model.Location;
import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.SessionRepository;
import dev.cheercode.weather_viewer.repository.UserRepository;
import dev.cheercode.weather_viewer.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Controller
public class HomeController {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final WeatherService weatherService;

    @GetMapping("index")
    public String index(
            Model model,
            HttpServletRequest request,
            @RequestParam(name = "lang", defaultValue = "en") String lang) {
        String sessionIdAttribute = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionIdAttribute = cookie.getValue();
                    cookie.setMaxAge(30 * 60);
                    break;
                }
            }
        }

        boolean isAuthenticated = false;
        if (sessionIdAttribute != null && !sessionIdAttribute.isEmpty()) {
            UUID sessionId = UUID.fromString(sessionIdAttribute);
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                    isAuthenticated = true;
                    User user = session.getUser();
                    model.addAttribute("userLogin", user.getLogin());
                    List<WeatherDto> weatherData = weatherService.getWeather(user.getLocations(), lang);
                    model.addAttribute("weatherData", weatherData);
                } else {
                    sessionRepository.deleteById(sessionId);
                }
            }
        }

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("lang", lang);
        return "index";
    }

    @GetMapping("find")
    public String find(
            Model model,
            HttpServletRequest request,
            @RequestParam String city,
            @RequestParam(name = "lang", defaultValue = "en") String lang
    ) {
        String sessionIdAttribute = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionIdAttribute = cookie.getValue();
                    cookie.setMaxAge(30 * 60);
                    break;
                }
            }
        }

        boolean isAuthenticated = false;
        if (sessionIdAttribute != null && !sessionIdAttribute.isEmpty()) {
            UUID sessionId = UUID.fromString(sessionIdAttribute);
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                    isAuthenticated = true;
                    User user = session.getUser();
                    model.addAttribute("userLogin", user.getLogin());
                    List<WeatherDto> weatherData = weatherService.getWeather(user.getLocations(), lang);
                    model.addAttribute("weatherData", weatherData);

                    session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
                    sessionRepository.save(session);
                } else {
                    sessionRepository.deleteById(sessionId);
                }
            }
        }

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("lang", lang);

        List<LocationDto> locations = weatherService.findLocations(city);
        model.addAttribute("locations", locations);

        return "index";
    }

    @PostMapping("add-location")
    public String addLocation(
            Model model,
            HttpServletRequest request,
            @RequestParam String city,
            @RequestParam String country,
            @RequestParam String state,
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(name = "lang", defaultValue = "en") String lang
    ) {
        String sessionIdAttribute = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionIdAttribute = cookie.getValue();
                    cookie.setMaxAge(30 * 60);
                    break;
                }
            }
        }

        boolean isAuthenticated = false;
        if (sessionIdAttribute != null && !sessionIdAttribute.isEmpty()) {
            UUID sessionId = UUID.fromString(sessionIdAttribute);
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                    isAuthenticated = true;
                    User user = session.getUser();
                    model.addAttribute("userLogin", user.getLogin());


                    if (!model.containsAttribute("locations")) {
                        throw new UnknownAttributeException("Unknown attribute: " + "\"locations\"");
                    }

                    List<LocationDto> locations = (List<LocationDto>) model.getAttribute("locations");
                    LocationDto locationDto = locations.stream()
                            .filter(l ->
                                    l.city().equals(city) &&
                                    l.country().equals(country) &&
                                    l.state().equals(state) &&
                                    l.latitude().equals(latitude) &&
                                    l.longitude().equals(longitude)
                            )
                            .findFirst()
                            .get();

                    Location location = mapToDto(locationDto, user);
                    user.getLocations().add(location);
                    userRepository.save(user);
                    model.addAttribute("locations", new ArrayList<>());

                    List<WeatherDto> weatherData = (List<WeatherDto>) model.getAttribute("weatherData");
                    WeatherDto weatherDto = weatherService.getWeather(location, lang);
                    weatherData.add(weatherDto);
                    model.addAttribute("weatherData", weatherData);

                    session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
                    sessionRepository.save(session);
                } else {
                    sessionRepository.deleteById(sessionId);
                }
            }
        }

        if (!isAuthenticated) {
            return "sign-in";
        }

        return "index";
    }

    @PostMapping("delete-location")
    public String deleteLocation(
            Model model,
            HttpServletRequest request,
            @RequestParam String city,
            @RequestParam String country
    ) {
        String sessionIdAttribute = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionIdAttribute = cookie.getValue();
                    cookie.setMaxAge(30 * 60);
                    break;
                }
            }
        }

        boolean isAuthenticated = false;
        if (!sessionIdAttribute.isEmpty()) {
            UUID sessionId = UUID.fromString(sessionIdAttribute);
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();
                if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                    isAuthenticated = true;
                    User user = session.getUser();
                    model.addAttribute("userLogin", user.getLogin());

                    if (!model.containsAttribute("weatherData")) {
                        throw new UnknownAttributeException("Unknown attribute: " + "\"weatherData\"");
                    }

                    List<WeatherDto> weatherData = (List<WeatherDto>) model.getAttribute("weatherData");
                    WeatherDto weatherDto = weatherData.stream()
                            .filter(l ->
                                    l.city().equals(city) &&
                                    l.country().equals(country)
                            )
                            .findFirst()
                            .get();

                    Location location = user.getLocations().stream()
                            .filter(l -> l.getName().equals(weatherDto.city()))
                            .findFirst()
                            .get();
                    user.getLocations().remove(location);
                    userRepository.save(user);

                    weatherData.remove(weatherDto);
                    model.addAttribute("weatherData", weatherData);

                    session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
                    sessionRepository.save(session);
                } else {
                    sessionRepository.deleteById(sessionId);
                }
            }
        }

        if (!isAuthenticated) {
            return "sign-in";
        }

        return "index";
    }

    private Location mapToDto(LocationDto locationDto, User user) {
        Location location = new Location();
        location.setName(locationDto.city());
        location.setUser(user);
        location.setLatitude(locationDto.latitude());
        location.setLongitude(locationDto.longitude());
        return location;
    }
}
