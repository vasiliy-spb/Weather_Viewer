package dev.cheercode.weather_viewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/404")
    public String notFound() {
        return "404";
    }

    @GetMapping("/500")
    public String serverError(@RequestParam(required = false) String status, Model model) {
        if (status != null) {
            model.addAttribute("status", status);
        }
        return "500";
    }
}
