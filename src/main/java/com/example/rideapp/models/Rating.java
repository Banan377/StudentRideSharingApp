package com.example.rideapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rater_id", nullable = false)
    private Long raterId;

    @Column(name = "rated_user_id", nullable = false)
    private Long ratedUserId;

    // مطابق لعمود stars في Railway
    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "comment", length = 500)
    private String comment;

    // مطابق تمامًا لعمود rater_email
    @Column(name = "rater_email")
    private String raterEmail;

    // مطابق تمامًا لعمود rated_email
    @Column(name = "rated_email")
    private String ratedEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Rating() {
        this.createdAt = LocalDateTime.now();
    }

    // ------- Getters & Setters -------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRaterId() { return raterId; }
    public void setRaterId(Long raterId) { this.raterId = raterId; }

    public Long getRatedUserId() { return ratedUserId; }
    public void setRatedUserId(Long ratedUserId) { this.ratedUserId = ratedUserId; }

    public Integer getStars() { return stars; }
    public void setStars(Integer stars) { this.stars = stars; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getRaterEmail() { return raterEmail; }
    public void setRaterEmail(String raterEmail) { this.raterEmail = raterEmail; }

    public String getRatedEmail() { return ratedEmail; }
    public void setRatedEmail(String ratedEmail) { this.ratedEmail = ratedEmail; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    


    }