package com.example.go4luncch.autocomplete;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Predictions {

    @SerializedName("status")
    private final String status;

    @SerializedName("predictions")
    private final List<Prediction> predictions;

    public Predictions(String status, List<Prediction> predictions) {
        this.status = status;
        this.predictions = predictions;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

}
