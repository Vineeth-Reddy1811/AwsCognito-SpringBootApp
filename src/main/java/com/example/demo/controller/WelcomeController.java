package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome To The Index Page!");
        return "index";
    }
    
    @GetMapping("/admin/greeting")
    public String adminGreet(Model model) {
        String response = "Welcome admin ! You developed an amazing website! :)";
        model.addAttribute("response", response);
        return "greeting";
    }
    @GetMapping("/user/greeting")
    public String userGreet(Model model) {
        String response = "Welcome user ! God bless you with amazing future ahead! :)";
        model.addAttribute("response", response);
        return "greeting";
    }
}