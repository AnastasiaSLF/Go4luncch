package com.example.go4luncch.PlaceDetails;

import com.google.gson.annotations.SerializedName;
import com.example.go4luncch.NearbySearch.Photo;
import java.util.List;
import com.example.go4luncch.NearbySearch.Geometry;

@SuppressWarnings("unused")
public class PlaceDetailsResult {

    @SerializedName("place_id")
    private String place_id;

    @SerializedName("name")
    private String name;

    @SerializedName("opening_hours")
    private OpeningHours opening_hours;

    @SerializedName("rating")
    private Float rating;

    @SerializedName("website")
    private String website;

    @SerializedName("international_phone_number")
    private String international_phone_number;

    @SerializedName("formatted_address")
    private String formatted_address;

    @SerializedName("photos")
    private List<Photo> photos;

    @SerializedName("geometry")
    private com.example.go4luncch.NearbySearch.Geometry geometry;

    @SerializedName("vicinity")
    private String vicinity;



    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }
}
