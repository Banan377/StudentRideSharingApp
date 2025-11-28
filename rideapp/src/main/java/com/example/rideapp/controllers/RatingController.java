package com.example.rideapp.controller;

import com.example.rideapp.models.RatingModel;
import com.example.rideapp.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // Get all ratings
    @GetMapping
    public List<RatingModel> getAllRatings() {
        return ratingService.getAllRatings();
    }

    // Get rating by ID
    @GetMapping("/{id}")
    public ResponseEntity<RatingModel> getRatingById(@PathVariable Long id) {
        Optional<RatingModel> rating = ratingService.getRatingById(id);
        return rating.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // Create new rating
    @PostMapping
    public RatingModel createRating(@RequestBody RatingModel rating) {
        return ratingService.createRating(rating);
    }

    // Get ratings by user (who received the rating)
    @GetMapping("/user/{userId}")
    public List<RatingModel> getRatingsByUser(@PathVariable Long userId) {
        return ratingService.getRatingsByUser(userId);
    }

    // Get average rating for user
    @GetMapping("/user/{userId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long userId) {
        Double average = ratingService.getAverageRating(userId);
        return ResponseEntity.ok(average);
    }
}