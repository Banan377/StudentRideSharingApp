package com.example.rideapp.repositories;

import com.example.rideapp.models.RatingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingModel, Long> {
    List<RatingModel> findByRatedId(Long userId);
    List<RatingModel> findByRaterId(Long userId);
}