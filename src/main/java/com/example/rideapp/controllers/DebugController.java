package com.example.rideapp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DebugController {

    @Value("${spring.mail.username:empty}")
    private String mailUser;

    @GetMapping("/api/debug/mail")
    public Map<String, String> mailConfig() {
        return Map.of("username", mailUser);
    }
}
