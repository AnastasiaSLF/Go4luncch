package com.example.go4luncch.repositories;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4luncch.BuildConfig;
import com.example.go4luncch.NearbySearch.PlacesResult;
import com.example.go4luncch.NearbySearch.Result;
import com.example.go4luncch.utils.APIClient;
import com.example.go4luncch.utils.ApiRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepository {
    private static final String TAG = "PlacesRepository";
    public static final String API_MAP_KEYWORD = "restaurant";

    private static final MutableLiveData<List<Result>> listenNearbyPlacesResults = new MutableLiveData<>();

    public MutableLiveData<List<Result>> getNearbyPlaces(String userLocationStr) {
        ApiRequest apiMap = APIClient.getClient().create(ApiRequest.class);
        if (userLocationStr != null) {
            Call<PlacesResult> nearbyPlaces = apiMap.getNearbyPlaces(
                    userLocationStr,
                    Resources.getSystem().getConfiguration().locale.getLanguage(),
                    API_MAP_KEYWORD,
                    BuildConfig.MAPS_API_KEY);

            nearbyPlaces.enqueue(new Callback<PlacesResult>() {
                @Override
                public void onResponse(@NonNull Call<PlacesResult> call, @NonNull Response<PlacesResult> response) {
                    if (response.isSuccessful()) {
                        PlacesResult body = response.body();
                        if (body != null) {
                            listenNearbyPlacesResults.setValue(body.getResults());
                            // Handle more than 40 results
                            if (body.getNextPageToken() != null) {
                                getNearbyPlacesNextPage(body.getNextPageToken());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<PlacesResult> call, @NonNull Throwable t) {
                    Log.d(TAG, "getPlace failure" + t);
                }

            });
        }
        return listenNearbyPlacesResults;
    }

    public static void getNearbyPlacesNextPage(String nextPageToken) {
        ApiRequest apiMap = APIClient.getClient().create(ApiRequest.class);
        Call<PlacesResult> nearbyPlacesNextPage = apiMap.getNearbyPlacesNextPage(
                nextPageToken,
                BuildConfig.MAPS_API_KEY);

        nearbyPlacesNextPage.enqueue(new Callback<PlacesResult>() {
            @Override
            public void onResponse(@NonNull Call<PlacesResult> call, @NonNull Response<PlacesResult> response) {
                if (response.isSuccessful()) {
                    PlacesResult body = response.body();
                    if (body != null) {
                        ArrayList<Result> resultsWithNextPageToken = new ArrayList<>();
                        resultsWithNextPageToken.addAll(Objects.requireNonNull(listenNearbyPlacesResults.getValue()));
                        resultsWithNextPageToken.addAll(body.getResults());
                        listenNearbyPlacesResults.setValue(resultsWithNextPageToken);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlacesResult> call, @NonNull Throwable t) {
                Log.d(TAG, "getPlace failure" + t);
            }

        });
    }

    public MutableLiveData<List<Result>> lRestaurants() {
        return listenNearbyPlacesResults;
    }
}