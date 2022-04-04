package com.example.go4luncch.models;

public class RestaurantChoice {
    String userId;
    String chosenRestaurantId;
    String chosenRestaurantName;
    String chosenRestaurantAddress;
    String chosenDate;

    public RestaurantChoice(String userId, String chosenRestaurantId, String chosenRestaurantName,
                            String chosenRestaurantAddress, String chosenDate) {
        this.userId = userId;
        this.chosenRestaurantId = chosenRestaurantId;
        this.chosenRestaurantName = chosenRestaurantName;
        this.chosenRestaurantAddress = chosenRestaurantAddress;
        this.chosenDate = chosenDate;
    }

    public RestaurantChoice() {
    }

    public String getChosenRestaurantId() {
        return chosenRestaurantId;
    }

    public void setChosenRestaurantId(String chosenRestaurantId) {
        this.chosenRestaurantId = chosenRestaurantId;
    }

    public String getChosenRestaurantName() {
        return chosenRestaurantName;
    }

    public void setChosenRestaurantName(String chosenRestaurantName) {
        this.chosenRestaurantName = chosenRestaurantName;
    }

    public String getChosenRestaurantAddress() {
        return chosenRestaurantAddress;
    }

    public void setChosenRestaurantAddress(String chosenRestaurantAddress) {
        this.chosenRestaurantAddress = chosenRestaurantAddress;
    }

    public String getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(String chosenDate) {
        this.chosenDate = chosenDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}