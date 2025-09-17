package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.exception.AuthenticationException;
import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.SessionRepository;
import dev.cheercode.weather_viewer.repository.UserRepository;
import dev.cheercode.weather_viewer.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";
    private static final Pattern STRONG_PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${session.lifetime}")
    private int sessionLifetime;

    public Session signUp(String login, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new AuthenticationException("Passwords don't match");
        }

        if (!isPasswordStrong(password)) {
            String notStrongPasswordMessage = "Password is too weak";
            throw new AuthenticationException(notStrongPasswordMessage);
        }

        try {
            User user = createUser(login, password);

            return createSession(user);

        } catch (DataIntegrityViolationException e) {
            if (isUniqueConstraintViolation(e)) {
                throw new AuthenticationException("User already exists with login: " + login);
            }
            throw e;
        }
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
            session.setExpiresAt(LocalDateTime.now().plusMinutes(sessionLifetime));
            sessionRepository.save(session);
        } else {
            session = createSession(user);
        }

        return session;
    }

    public void logout(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    private boolean isPasswordStrong(String password) {
        return STRONG_PASSWORD.matcher(password).matches();
    }

    private User createUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private Session createSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(sessionLifetime));
        return sessionRepository.save(session);
    }

    private boolean isUniqueConstraintViolation(DataIntegrityViolationException e) {
        if (e.getCause() != null && e.getRootCause() instanceof SQLException cause) {
            String sqlState = cause.getSQLState();
            return sqlState.equals(UNIQUE_VIOLATION_SQL_STATE);
        }
        return false;
    }
}
