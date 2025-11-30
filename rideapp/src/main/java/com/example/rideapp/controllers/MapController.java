package com.example.rideapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.rideapp.services.MapService;

@RestController
@RequestMapping("/api/map")
public class MapController {
    @Autowired private MapService mapService;

    @GetMapping("/route")
    public ResponseEntity<?> getRoute(@RequestParam double fromLat, @RequestParam double fromLng, @RequestParam double toLat, @RequestParam double toLng) {
      var r = mapService.getRoute(fromLat, fromLng, toLat, toLng);
      if (r == null) return ResponseEntity.badRequest().body("No route found");
      return ResponseEntity.ok(r);
    }
}
