package dev.cheercode.weather_viewer.service;

import dev.cheercode.weather_viewer.exception.EntityAlreadyExistsException;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.UserRepository;
import dev.cheercode.weather_viewer.util.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new EntityAlreadyExistsException("User already exists: " + login);
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));

        password = "reset";
        return userRepository.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
