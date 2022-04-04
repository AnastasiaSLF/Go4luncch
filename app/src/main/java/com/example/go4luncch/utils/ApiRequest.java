package com.example.go4luncch.utils;

import com.example.go4luncch.NearbySearch.PlacesResult;
import com.example.go4luncch.PlaceDetails.PlaceDetail;
import com.example.go4luncch.autocomplete.Predictions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {


    @GET("place/nearbysearch/json?rankby=distance")
    Call<PlacesResult> getNearbyPlaces(
            @Query("location") String location,
            @Query("language") String language,
            @Query("keyword") String keyword,
            @Query("key") String key
    );

    @GET("place/nearbysearch/json")
    Call<PlacesResult> getNearbyPlacesNextPage(
            @Query("pagetoken") String pagetoken,
            @Query("key") String key
    );

    @GET("place/details/json")
    Call<PlaceDetail> getPlaceDetails(
            @Query("place_id") String place_id,
            @Query("fields") String fields,
            @Query("language") String language,
            @Query("key") String key
    );
    @GET("place/autocomplete/json")
    Call<Predictions> getAutocomplete(
            @Query("location") String location,
            @Query("radius") Integer radius,
            @Query("input") String input,
            @Query("types") String types,
            @Query("strictbounds") String strictbounds,
            @Query("key") String key

    );


}