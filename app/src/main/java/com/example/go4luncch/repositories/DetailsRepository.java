package com.example.go4luncch.repositories;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4luncch.BuildConfig;
import com.example.go4luncch.PlaceDetails.PlaceDetail;
import com.example.go4luncch.PlaceDetails.PlaceDetailsResult;
import com.example.go4luncch.utils.APIClient;
import com.example.go4luncch.utils.ApiRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsRepository {

    public DetailsRepository() {
    }
    private static final String TAG = "PlaceDetailsService";
    private static final String API_MAP_FIELDS = "formatted_address,geometry,photos,vicinity," +
            "place_id,name,rating,opening_hours,website,international_phone_number";
    public static final MutableLiveData<PlaceDetailsResult> placeDetailResults = new MutableLiveData<>();

    public MutableLiveData<PlaceDetailsResult> getPlaceDetails(String placeId) {
        ApiRequest apiDetails = APIClient.getClient().create(ApiRequest.class);
        Call<PlaceDetail> placeDetails = apiDetails.getPlaceDetails(
                placeId,
                API_MAP_FIELDS,
                Resources.getSystem().getConfiguration().locale.getLanguage(),
                BuildConfig.MAPS_API_KEY);

        placeDetails.enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetail> call,
                                   @NonNull Response<PlaceDetail> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    PlaceDetail body = response.body();
                    if (body != null) {
                        placeDetailResults.setValue(body.getResult());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetail> call, @NonNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
                placeDetailResults.setValue(null);
            }

        });
        return placeDetailResults;
    }

    public MutableLiveData<PlaceDetailsResult> getRestaurantDetails() {
        return placeDetailResults;
    }

}


