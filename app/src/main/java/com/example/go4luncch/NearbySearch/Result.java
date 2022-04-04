package com.example.go4luncch.NearbySearch;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Result implements Serializable {

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private Float rating;

    @SerializedName("opening_hours")
    private OpeningHours open_now;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("photos")
    private List<Photo> photos;

    @SerializedName("types")
    private List<String> types;

    // --- GETTERS --- //

    public Result(String placeId, String name, Float rating, OpeningHours open_now,
                        String vicinity, Geometry geometry,
                        List<Photo> photos, List<String> types) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.open_now = open_now;
        this.vicinity = vicinity;
        this.geometry = geometry;
        this.photos = photos;
        this.types = types;
    }

    public Result(String placeId, String name, Float rating, OpeningHours open_now,
                        String vicinity, Location restaurantLocation,
                        List<Photo> photos) {
    }

    public Result() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public Float getRating() {
        return rating;
    }

    public OpeningHours getOpenNow() {
        return open_now;
    }

    public String getVicinity() {
        return vicinity;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<String> getTypes() {
        return types;
    }

    // --- SETTERS --- For test Purpose //
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public OpeningHours getOpen_now() {
        return open_now;
    }

    public void setOpen_now(OpeningHours open_now) {
        this.open_now = open_now;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }


    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }


}
