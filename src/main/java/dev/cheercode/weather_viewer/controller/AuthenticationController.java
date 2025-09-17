package dev.cheercode.weather_viewer.controller;

import dev.cheercode.weather_viewer.exception.AuthenticationException;
import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@AllArgsConstructor
@Controller
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("sign-up")
    public String getSignUp() {
        return "sign-up";
    }


    @PostMapping("sign-up")
    public String postSignUp(
            Model model,
            HttpServletResponse response,
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        try {
            Session session = authenticationService.signUp(login, password, confirmPassword);

            addSessionCookie(response, session);

            return "redirect:/index";
        } catch (AuthenticationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "sign-up";
        }
    }

    @GetMapping("sign-in")
    public String getSignIn() {
        return "sign-in";
    }

    @PostMapping("sign-in")
    public String postSignIn(
            Model model,
            HttpServletResponse response,
            @RequestParam String login,
            @RequestParam String password
    ) {
        try {
            Session session = authenticationService.signIn(login, password);

            addSessionCookie(response, session);

            return "redirect:/index";
        } catch (AuthenticationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "sign-in";
        }
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request) {
        String sessionIdAttribute = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("sessionId")) {
                sessionIdAttribute = cookie.getValue();
                cookie.setMaxAge(0);
                break;
            }
        }

        if (sessionIdAttribute == null) {
            throw new AuthenticationException("Session id attribute not found");
        }

        try {
            UUID uuid = UUID.fromString(sessionIdAttribute);
            authenticationService.logout(uuid);
        } catch (IllegalArgumentException ignored) {
        }

        return "redirect:/index";
    }

    private void addSessionCookie(HttpServletResponse response, Session session) {
        Cookie sessionCookie = new Cookie("sessionId", session.getId().toString());
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(30 * 60);
        response.addCookie(sessionCookie);
    }
}
