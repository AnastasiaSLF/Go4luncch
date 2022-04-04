package com.example.go4luncch;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4luncch.repositories.AutocompleteRepository;
import com.example.go4luncch.repositories.DetailsRepository;
import com.example.go4luncch.repositories.LocationRepository;
import com.example.go4luncch.repositories.PlacesRepository;
import com.example.go4luncch.repositories.SharedPreferencesRepository;
import com.example.go4luncch.repositories.UserLikedRepository;
import com.example.go4luncch.repositories.UserRepository;
import com.example.go4luncch.utils.Permissions;
import com.example.go4luncch.viewmodels.DetailsViewmodel;
import com.example.go4luncch.viewmodels.RestaurantsViewModel;
import com.example.go4luncch.viewmodels.SettingViewModel;
import com.example.go4luncch.viewmodels.WorkmatesViewModel;
import com.google.android.gms.location.LocationServices;

public class ViewModelFactory implements ViewModelProvider.Factory {

private volatile static ViewModelFactory sInstance;

@NonNull
private final Permissions permissionChecker;

@NonNull
private final LocationRepository locationRepository;

@NonNull
private final PlacesRepository nearByPlacesRepository;

@NonNull
private final UserRepository userRepository;
private final DetailsRepository detailRestaurantRepository;
        private final UserLikedRepository userLikingRestaurantRepository;
        private final SharedPreferencesRepository sharedPreferencesRepository;
        private final AutocompleteRepository autocompleteRepository;


        public static ViewModelFactory getInstance() {
        if (sInstance == null) {
synchronized (ViewModelFactory.class) {
        if (sInstance == null) {
                Application application = BaseApplication.getApplication();

        sInstance = new ViewModelFactory(
        new Permissions(application),
        new LocationRepository(
        LocationServices.getFusedLocationProviderClient(application)),
        new PlacesRepository(),
        new UserRepository(),
        new DetailsRepository(),
        new UserLikedRepository(),
        new SharedPreferencesRepository(),
        new AutocompleteRepository());

        }
        }
        }

        return sInstance;
        }

private ViewModelFactory(
@NonNull Permissions permissionChecker,
@NonNull LocationRepository locationRepository,
@NonNull PlacesRepository nearByPlacesRepository,
@NonNull UserRepository userRepository,
@NonNull DetailsRepository detailRestaurantRepository,
UserLikedRepository userLikingRestaurantRepository,
SharedPreferencesRepository sharedPreferencesRepository,
AutocompleteRepository autoCompleteRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.nearByPlacesRepository = nearByPlacesRepository;
        this.userRepository = userRepository;
        this.detailRestaurantRepository = detailRestaurantRepository;
        this.userLikingRestaurantRepository = userLikingRestaurantRepository;
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.autocompleteRepository = autoCompleteRepository;


}

@SuppressWarnings("unchecked")
@NonNull
@Override
public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
        return (T) new RestaurantsViewModel(
        permissionChecker,
        locationRepository,
        nearByPlacesRepository,
        userRepository,
                autocompleteRepository,
                detailRestaurantRepository
        );
        } else if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
                return (T) new WorkmatesViewModel(
                        userRepository
                );
        } else if (modelClass.isAssignableFrom(DetailsViewmodel.class)) {
                return (T) new DetailsViewmodel(
                        detailRestaurantRepository,
                        userRepository,
                        userLikingRestaurantRepository
                );
        } else if (modelClass.isAssignableFrom(SettingViewModel.class)) {
                return (T) new SettingViewModel(
                        sharedPreferencesRepository
                );
        } else
                throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
}
}