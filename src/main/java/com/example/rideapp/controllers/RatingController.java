package com.example.rideapp.controllers;

import com.example.rideapp.models.Rating;
import com.example.rideapp.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;

    // ------------------ Health Check ------------------
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Rating Service");
        response.put("message", "الخدمة تعمل بنجاح!");
        return ResponseEntity.ok(response);
    }


    // ------------------ Create Rating ------------------
    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("استلام طلب تقييم: " + request);

            if (!request.containsKey("stars")) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "قيمة النجوم مطلوبة"
                ));
            }

            Integer stars = Integer.valueOf(request.get("stars").toString());
            if (stars < 1 || stars > 5) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "عدد النجوم يجب أن يكون بين 1 و 5"
                ));
            }

            Rating rating = new Rating();
            Object raterIdObj = request.get("raterId");
Object ratedUserIdObj = request.get("ratedUserId");

if (raterIdObj == null || ratedUserIdObj == null) {
    return ResponseEntity.badRequest().body(Map.of(
        "success", false,
        "message", "الـ raterId أو ratedUserId غير موجود!"
    ));
}

Long raterId = Long.valueOf(raterIdObj.toString());
Long ratedUserId = Long.valueOf(ratedUserIdObj.toString());

rating.setRaterId(raterId);
rating.setRatedUserId(ratedUserId);

            rating.setStars(stars);
Object c = request.get("comment");
rating.setComment(c != null ? c.toString() : "");

            Rating savedRating = ratingRepository.save(rating);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "تم إرسال التقييم بنجاح",
                    "ratingId", savedRating.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }


    // ------------------ Get Ratings for One User ------------------
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
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }


    // ------------------ Average Rating ------------------
    @GetMapping("/user/{userId}/average")
    public ResponseEntity<?> getUserAverageRating(@PathVariable Long userId) {
        try {
            List<Rating> ratings = ratingRepository.findByRatedUserId(userId);

            double average = ratings.stream()
                    .mapToInt(Rating::getStars)
                    .average()
                    .orElse(0.0);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("averageRating", Math.round(average * 10.0) / 10.0);
            response.put("totalRatings", ratings.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", userId,
                    "averageRating", 0.0,
                    "totalRatings", 0
            ));
        }
    }


    // ------------------ All Ratings ------------------
    @GetMapping
    public ResponseEntity<?> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("ratings", ratings);
        response.put("count", ratings.size());

        return ResponseEntity.ok(response);
    }

}
