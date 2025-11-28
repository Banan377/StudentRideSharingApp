package com.example.rideapp.service;

import com.example.rideapp.models.RatingModel;
import com.example.rideapp.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<RatingModel> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Optional<RatingModel> getRatingById(Long id) {
        return ratingRepository.findById(id);
    }

    public RatingModel createRating(RatingModel rating) {
        return ratingRepository.save(rating);
    }

    public List<RatingModel> getRatingsByUser(Long userId) {
        return ratingRepository.findByRatedId(userId);
    }

    public Double getAverageRating(Long userId) {
        List<RatingModel> ratings = ratingRepository.findByRatedId(userId);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        
        double sum = ratings.stream()
                .mapToInt(RatingModel::getRating)
                .sum();
        
        return sum / ratings.size();
    }
}