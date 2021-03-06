package com.example.go4luncch.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlacesResult {
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<>();
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private ArrayList<Result> results = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * @return The htmlAttributions
     */
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     * @param htmlAttributions The html_attributions
     */
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     * @return The nextPageToken
     */
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     * @param nextPageToken The next_page_token
     */
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     * @return The results
     */
    public ArrayList<Result> getResults() {
        return results;
    }


    /**
     * @param results The results
     */
    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}

