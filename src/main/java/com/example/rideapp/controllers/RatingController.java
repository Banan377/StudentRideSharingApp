package com.example.rideapp.controllers;

import com.example.rideapp.models.Rating;
import com.example.rideapp.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;
    

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Rating Service");
        response.put("message", "الخدمة تعمل بنجاح!");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody Map<String, Object> request) {
        try {
            System.out.println(" استلام طلب تقييم: " + request);

            Rating rating = new Rating();
            rating.setRaterId(Long.valueOf(request.get("raterId").toString()));
            rating.setRatedUserId(Long.valueOf(request.get("ratedUserId").toString()));
            rating.setRideId(Long.valueOf(request.get("rideId").toString()));
            rating.setRatingValue(Integer.valueOf(request.get("ratingValue").toString()));
            rating.setComment((String) request.get("comment"));

            Rating savedRating = ratingRepository.save(rating);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "تم إرسال التقييم بنجاح");
            response.put("ratingId", savedRating.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserRatings(@PathVariable Long userId) {
        try {
            List<Rating> ratings = ratingRepository.findByRatedUserId(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ratings", ratings);
            response.put("count", ratings.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}/average")
    public ResponseEntity<?> getUserAverageRating(@PathVariable Long userId) {
        try {
            List<Rating> ratings = ratingRepository.findByRatedUserId(userId);
            double average = ratings.stream()
                    .mapToInt(Rating::getRatingValue)
                    .average()
                    .orElse(0.0);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("averageRating", Math.round(average * 10.0) / 10.0);
            response.put("totalRatings", ratings.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("averageRating", 0.0);
            response.put("totalRatings", 0);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("ratings", ratings);
        response.put("count", ratings.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-ratings")
    public ResponseEntity<?> getLowRatings() {
        try {
            List<Rating> lowRatings = ratingRepository.findAll().stream()
                    .filter(r -> r.getRatingValue() <= 2)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", lowRatings.size());
            response.put("ratings", lowRatings);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
