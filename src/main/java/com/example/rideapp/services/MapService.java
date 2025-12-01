package com.example.rideapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class MapService {
  @Value("${GOOGLE_MAPS_API_KEY}")
  private String apiKey;

  private final RestTemplate restTemplate = new RestTemplate();

  public MapRoute getRoute(double fromLat, double fromLng, double toLat, double toLng) {
    String url = UriComponentsBuilder.fromUriString("https://maps.googleapis.com/maps/api/directions/json")
      .queryParam("origin", fromLat + "," + fromLng)
      .queryParam("destination", toLat + "," + toLng)
      .queryParam("key", apiKey)
      .queryParam("mode", "driving")
      .toUriString();
    ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
    Map body = resp.getBody();
    if (body == null) return null;
    var routes = (java.util.List) body.get("routes");
    if (routes == null || routes.isEmpty()) return null;
    Map firstRoute = (Map) routes.get(0);
    Map overview = (Map) firstRoute.get("overview_polyline");
    String polyline = overview == null ? null : (String) overview.get("points");
    var legs = (java.util.List) firstRoute.get("legs");
    Map firstLeg = legs == null || legs.isEmpty() ? null : (Map) legs.get(0);
    Map distance = firstLeg == null ? null : (Map) firstLeg.get("distance");
    Integer meters = distance == null ? null : (Integer) distance.get("value");
    int metersVal = meters == null ? 0 : meters;
    return new MapRoute(polyline, metersVal);
  }

  public static class MapRoute {
    private String polyline;
    private int distanceMeters;
    public MapRoute(String polyline, int distanceMeters) { this.polyline = polyline; this.distanceMeters = distanceMeters; }
    public String getPolyline() { return polyline; }
    public int getDistanceMeters() { return distanceMeters; }
  }
}