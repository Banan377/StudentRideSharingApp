package com.example.rideapp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/google")
public class GoogleController {

    @Value("${google.maps.api-key}")
    private String googleMapsApiKey;

    @GetMapping("/maps-key")
    public String getMapsKey() {
        return googleMapsApiKey;
    }
}

