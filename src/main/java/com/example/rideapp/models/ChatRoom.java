
package com.example.rideapp.models;

import jakarta.persistence.*;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rideId;

    public ChatRoom() {}

    public ChatRoom(Long rideId) {
        this.rideId = rideId;
    }

    public Long getId() {
        return id;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
}

