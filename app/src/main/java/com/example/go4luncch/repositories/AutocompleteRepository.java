package com.example.go4luncch.repositories;

import static com.example.go4luncch.MainActivity.RADIUS_MAX;
import static com.example.go4luncch.fragments.MapsFragment.userLocation;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4luncch.BuildConfig;
import com.example.go4luncch.autocomplete.Prediction;
import com.example.go4luncch.autocomplete.Predictions;
import com.example.go4luncch.utils.APIClient;
import com.example.go4luncch.utils.ApiRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutocompleteRepository {
    private static final String TAG = "AutoCompleteRepository";

    public static final String API_AUTOCOMPLETE_KEYWORD = "establishment";

    public static final MutableLiveData<List<Prediction>> listenAutoCompletePredictions = new MutableLiveData<>();

    public void getAutocomplete(String input) {
        ApiRequest apiAutocomplete = APIClient.getClient().create(ApiRequest.class);
        Call<Predictions> autocompleteSearch = apiAutocomplete.getAutocomplete(
                userLocation,
                RADIUS_MAX,
                input,
                API_AUTOCOMPLETE_KEYWORD,
                "",
                BuildConfig.MAPS_API_KEY);
        autocompleteSearch.enqueue(new Callback<Predictions>() {
            @Override
            public void onResponse(@NonNull Call<Predictions> call,
                                   @NonNull Response<Predictions> response) {
                if (response.isSuccessful()) {
                    Predictions predictionsAPI = response.body();
                    if (predictionsAPI != null) {
                        listenAutoCompletePredictions.setValue(predictionsAPI.getPredictions());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Predictions> call,
                                  @NonNull Throwable t) {
                Log.d(TAG, "getPlace API failure" + t);
                listenAutoCompletePredictions.setValue(null);
            }
        });
    }

    public MutableLiveData<List<Prediction>> getListAutoComplete() {
        return listenAutoCompletePredictions;
    }
}


