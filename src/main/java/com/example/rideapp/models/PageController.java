package com.example.rideapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/passengerRides")
    public String passengerRides() {
        return "passengerRides.html";
    }

    @GetMapping("/rating")
    public String ratingPage() {
        return "rating.html";
    }
}
