package com.example.rideapp.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "map")
public class MapModel {
   
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapId;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private RideModel ride;

    private Double latitude;  // خط العرض لنقطة معينة من الرحلة
    private Double longitude; // خط الطول لنقطة معينة من الرحلة
    private LocalDateTime timestamp;  // توقيت أخذ الموقع

    public MapModel() {
    }

    public void setMapId(Long mapId){
        this.mapId = mapId;
    }
    public Long getMapId(){
        return mapId;
    }
    public void setRide(RideModel ride){
        this.ride = ride;
    }
    public RideModel getRide(){
        return ride;
    }
    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }
    public Double getLatitude(){
        return latitude;
    }
    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }
    public Double getLongitude(){
        return longitude;
    }
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
    public LocalDateTime getTimestamp(){
        return timestamp;
    }
}
