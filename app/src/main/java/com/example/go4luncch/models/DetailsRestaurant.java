package com.example.go4luncch.models;

import com.example.go4luncch.PlaceDetails.OpeningHours;
import com.example.go4luncch.NearbySearch.Photo;

import java.util.List;

public class DetailsRestaurant {
    private String placeId;
    private String name;
    private OpeningHours openingHours;
    private Float rating;
    private String website;
    private String internationalPhoneNumber;
    private String formatted_address;
    private List<Photo> photos;
    private Boolean userLike;
    private String chosenRestaurantName;
    private String chosenRestaurantId;
    private String chosenRestaurantAddress;

    public DetailsRestaurant(String placeId, String name, OpeningHours openingHours, Float rating,
                                     String website, String internationalPhoneNumber, String formatted_address,
                                     List<Photo> photos,
                                     boolean b, Boolean userLike, String chosenRestaurantName, String chosenRestaurantId,
                                     String chosenRestaurantAddress) {
        this.placeId = placeId;
        this.name = name;
        this.openingHours = openingHours;
        this.rating = rating;
        this.website = website;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.formatted_address = formatted_address;
        this.photos = photos;
        this.userLike = userLike;
        this.chosenRestaurantName = chosenRestaurantName;
        this.chosenRestaurantId = chosenRestaurantId;
        this.chosenRestaurantAddress = chosenRestaurantAddress;
    }

    public DetailsRestaurant(String placeId, String name, OpeningHours opening_hours, Float rating, String website, String international_phone_number, String formatted_address, List<com.example.go4luncch.NearbySearch.Photo> photos, boolean b, Object userLike, Object choosenRestaurantName, Object choosenRestaurantId, Object choosenRestaurantAdress) {
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

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
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

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Boolean getUserLike() {
        return userLike;
    }

    public void setUserLike(Boolean userLike) {
        this.userLike = userLike;
    }

    public String getChoosenRestaurantName() {
        return chosenRestaurantName;
    }

    public void setChoosenRestaurantName(String choosenRestaurantName) {
        this.chosenRestaurantName = choosenRestaurantName;
    }

    public String getChoosenRestaurantId() {
        return chosenRestaurantId;
    }

    public void setChoosenRestaurantId(String choosenRestaurantId) {
        this.chosenRestaurantId = choosenRestaurantId;
    }

    public String getChoosenRestaurantAdress() {
        return chosenRestaurantAddress;
    }

    public void setChoosenRestaurantAdress(String choosenRestaurantAdress) {
        this.chosenRestaurantAddress = choosenRestaurantAdress;
    }
}
