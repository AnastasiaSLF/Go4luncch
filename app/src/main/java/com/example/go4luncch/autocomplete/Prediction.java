package com.example.go4luncch.autocomplete;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Prediction {
    @SerializedName("description")
    private final String description;

    @SerializedName("place_id")
    private final String place_id;

    @SerializedName("structured_formatting")
    private final TextAutocomplete structured_formatting;

    @SerializedName("types")
    private final List<String> types;

    public Prediction(String description, String place_id, TextAutocomplete structured_formatting, List<String> types) {
        this.description = description;
        this.place_id = place_id;
        this.structured_formatting = structured_formatting;
        this.types = types;
    }

    public String getDescription() {
        return description;
    }

    public String getPlace_id() {
        return place_id;
    }

    public TextAutocomplete getStructured_formatting() {
        return structured_formatting;
    }

    public List<String> getTypes() {
        return types;
    }


}

