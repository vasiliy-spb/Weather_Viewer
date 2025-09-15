package dev.cheercode.weather_viewer.controller;

import dev.cheercode.weather_viewer.dto.LocationDto;
import dev.cheercode.weather_viewer.dto.WeatherDto;
import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.SessionRepository;
import dev.cheercode.weather_viewer.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Controller
public class HomeController {
    private final SessionRepository sessionRepository;
    private final WeatherService weatherService;

    @GetMapping("index")
    public String index(
            Model model,
            @RequestParam(name = "lang", defaultValue = "en") String lang) {
        String sessionIdAttribute = Objects.toString(model.getAttribute("sessionId"), "");
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
            @RequestParam String city,
            @RequestParam(name = "lang", defaultValue = "en") String lang
    ) {
        String sessionIdAttribute = Objects.toString(model.getAttribute("sessionId"), "");
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
                    List<WeatherDto> weatherData = weatherService.getWeather(user.getLocations(), lang);
                    model.addAttribute("weatherData", weatherData);
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
}
