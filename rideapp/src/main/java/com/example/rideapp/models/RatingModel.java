package com.example.rideapp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class RatingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer rating; // 1-5 stars
    
    @Column(length = 500)
    private String comment;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // ✅ التعديل هنا
    @ManyToOne
    @JoinColumn(name = "rater_email", referencedColumnName = "email", nullable = false)
    private UserModel rater;
    
    // ✅ التعديل هنا
    @ManyToOne
    @JoinColumn(name = "rated_email", referencedColumnName = "email", nullable = false)
    private UserModel rated;
    
    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private RideModel ride;

    // Constructors
    public RatingModel() {}
    
    public RatingModel(Integer rating, String comment, LocalDateTime createdAt, UserModel rater, UserModel rated, RideModel ride) {
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.rater = rater;
        this.rated = rated;
        this.ride = ride;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public UserModel getRater() { return rater; }
    public void setRater(UserModel rater) { this.rater = rater; }
    
    public UserModel getRated() { return rated; }
    public void setRated(UserModel rated) { this.rated = rated; }
    
    public RideModel getRide() { return ride; }
    public void setRide(RideModel ride) { this.ride = ride; }
}