package dev.cheercode.weather_viewer.controller;

import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.SessionRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Controller
public class HomeController {
    private final SessionRepository sessionRepository;

    @GetMapping("index")
    public String getIndex(HttpSession httpSession, Model model) {

        UUID sessionId = (UUID) httpSession.getAttribute("sessionId");

        if (sessionId != null) {
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);

            if (sessionOptional.isPresent()) {
                Session session = sessionOptional.get();

                if (session.getExpiresAt().isAfter(LocalDateTime.now())) {
                    User user = session.getUser();
                    model.addAttribute("authenticated", true);
                    model.addAttribute("username", user.getLogin());
                    return "index";
                } else {
                    sessionRepository.deleteById(sessionId);
                    httpSession.removeAttribute("sessionId");
                }
            }
        } else {
            httpSession.removeAttribute("sessionId");
        }

        model.addAttribute("authenticated", false);
        return "index";
    }
}
