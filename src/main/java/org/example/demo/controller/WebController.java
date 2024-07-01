package org.example.demo.controller;

import org.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class WebController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/main")
    public String showMainPage(Model model, Principal principal) {
        String email = principal.getName();
        model.addAttribute("email", email);
//        System.out.println("email: " + email);
        return "main";
    }

    @GetMapping("/forgot")
    public String showForgotPage() { return "forgot"; }



}
