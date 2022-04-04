package com.example.go4luncch.models;



import com.example.go4luncch.NearbySearch.Location;
import com.example.go4luncch.NearbySearch.Photo;

import java.util.List;

public class Restaurant  {
    private String placeId;
    private String name;
    private Float rating;
    private Boolean open_false;
    private String vicinity;
    private Location restaurantLocation;
    private Integer restaurantDistance;
    private List<Photo> photos;
    private Integer nbWorkmates;


    public Restaurant(String placeId, String name, Float rating, Boolean open_false,
                               String vicinity, Location restaurantLocation, Integer restaurantDistance,
                               List<Photo> photos, Integer nbWorkmates) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.open_false = open_false;
        this.vicinity = vicinity;
        this.restaurantLocation = restaurantLocation;
        this.restaurantDistance = restaurantDistance;
        this.photos = photos;
        this.nbWorkmates = nbWorkmates;

    }



    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setOpen_false(Boolean open_false) {
        this.open_false = open_false;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(Location restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public Integer getRestaurantDistance() {
        return restaurantDistance;
    }

    public void setRestaurantDistance(Integer restaurantDistance) {
        this.restaurantDistance = restaurantDistance;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Boolean getOpenNow() {
        return open_false;
    }


    public Integer getNbWorkmates() {
        return nbWorkmates;
    }

    public void setNbWorkmates(Integer nbWorkmates) {
        this.nbWorkmates = nbWorkmates;
    }
}

