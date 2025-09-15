package dev.cheercode.weather_viewer.controller;

import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.model.User;
import dev.cheercode.weather_viewer.repository.SessionRepository;
import dev.cheercode.weather_viewer.repository.UserRepository;
import dev.cheercode.weather_viewer.service.SessionService;
import dev.cheercode.weather_viewer.util.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class SessionController {
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("signin")
    public String getSignIn() {
        return "signin";
    }

    @GetMapping("signup")
    public String getSignUp() {
        return "signup";
    }


    @PostMapping("signup")
    public String postSignUp(
            Model model,
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords don't match");
            return "signup";
        }

        if (!isPasswordStrong(password)) {
            model.addAttribute("errorMessage", "Password is not strong enough");
            return "signup";
        }

        if (userRepository.existsByLogin(login)) {
            model.addAttribute("errorMessage", "User with this login already exists");
            return "signup";
        }

        try {
            String encodedPassword = passwordEncoder.encode(password);
            Session session = sessionService.signUp(login, encodedPassword);
            model.addAttribute("sessionId", session.getId());
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "signup";
        }
    }

    @PostMapping
    public String signin(
            Model model,
            @RequestParam String login,
            @RequestParam String password
    ) {
        if (!userRepository.existsByLogin(login)) {
            model.addAttribute("errorMessage", "Incorrect login or password");
            return "signin";
        }

        User user = userRepository.findByLogin(login).get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("errorMessage", "Incorrect login or password");
            return "signin";
        }

        Optional<Session> sessionOptional = sessionRepository.findByUserId(user.getId());
        Session session;
        if (sessionOptional.isPresent()) {
            session = sessionOptional.get();
            session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            sessionRepository.save(session);
        } else {
            session = createSession(user);
        }

        model.addAttribute("sessionId", session.getId());

        return "index";
    }

    private Session createSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        return sessionRepository.save(session);
    }

    private boolean isPasswordStrong(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(pattern);
    }
}
