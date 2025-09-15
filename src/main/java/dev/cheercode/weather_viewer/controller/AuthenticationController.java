package dev.cheercode.weather_viewer.controller;

import dev.cheercode.weather_viewer.exception.AuthenticationException;
import dev.cheercode.weather_viewer.model.Session;
import dev.cheercode.weather_viewer.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        try {
            Session session = authenticationService.signUp(login, password, confirmPassword);
            model.addAttribute("sessionId", session.getId());
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
            @RequestParam String login,
            @RequestParam String password
    ) {
        try {
            Session session = authenticationService.signIn(login, password);
            model.addAttribute("sessionId", session.getId());
            return "redirect:/index";
        } catch (AuthenticationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "sign-in";
        }
    }
}
