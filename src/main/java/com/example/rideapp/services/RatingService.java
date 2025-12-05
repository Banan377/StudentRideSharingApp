package com.example.rideapp.services;

import com.example.rideapp.models.Rating;
import com.example.rideapp.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List<Rating> getUserRatings(Long userId) {
        return ratingRepository.findByRatedUserId(userId);
    }

    public Double calculateAverageRating(Long userId) {
        List<Rating> ratings = ratingRepository.findByRatedUserId(userId);
        return ratings.stream()
            .mapToInt(Rating::getRatingValue)
            .average()
            .orElse(0.0);
    }
}