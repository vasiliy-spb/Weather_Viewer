package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.SessionRepository;
import dev.cheercode.weather_viewer.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public Session signUp(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseGet(() -> createUser(login, password));

        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        return sessionRepository.save(session);
    }

    private User createUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        return userRepository.save(user);
    }
}
