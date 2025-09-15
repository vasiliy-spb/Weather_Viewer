package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.exception.AuthenticationException;
import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.SessionRepository;
import dev.cheercode.weather_viewer.repository.UserRepository;
import dev.cheercode.weather_viewer.util.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private static final int SESSION_LIFETIME = 30;
    private static final Pattern STRONG_PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Session signUp(String login, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new AuthenticationException("Passwords don't match");
        }

        if (!isPasswordStrong(password)) {
            String notStrongPasswordMessage = """
                    Password is not strong enough:
                    - At least 8 characters long
                    - Include both uppercase (A-Z) and lowercase (a-z) letters
                    - Include at least one number (0-9)
                    """;
            throw new AuthenticationException(notStrongPasswordMessage);
        }

        if (userRepository.existsByLogin(login)) {
            throw new AuthenticationException("User already exists with login: " + login);
        }

        User user = createUser(login, password);

        return createSession(user);
    }

    public Session signIn(String login, String password) {
        if (!userRepository.existsByLogin(login)) {
            throw new AuthenticationException("Incorrect login or password");
        }

        User user = userRepository.findByLogin(login).get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Incorrect login or password");
        }

        Optional<Session> sessionOptional = sessionRepository.findByUserId(user.getId());
        Session session;
        if (sessionOptional.isPresent()) {
            session = sessionOptional.get();
            session.setExpiresAt(LocalDateTime.now().plusMinutes(SESSION_LIFETIME));
        } else {
            session = createSession(user);
        }

        return session;
    }

    private Session createSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(SESSION_LIFETIME));
        return sessionRepository.save(session);
    }

    private User createUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private boolean isPasswordStrong(String password) {
        return STRONG_PASSWORD.matcher(password).matches();
    }
}
