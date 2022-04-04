package com.example.go4luncch.viewmodels;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4luncch.NearbySearch.Geometry;
import com.example.go4luncch.NearbySearch.Location;
import com.example.go4luncch.NearbySearch.OpeningHours;
import com.example.go4luncch.NearbySearch.Result;
import com.example.go4luncch.PlaceDetails.PlaceDetailsResult;
import com.example.go4luncch.autocomplete.Prediction;
import com.example.go4luncch.models.Restaurant;
import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.AutocompleteRepository;
import com.example.go4luncch.repositories.DetailsRepository;
import com.example.go4luncch.repositories.LocationRepository;
import com.example.go4luncch.repositories.PlacesRepository;
import com.example.go4luncch.repositories.UserRepository;
import com.example.go4luncch.utils.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RestaurantsViewModel extends ViewModel {
    PlacesRepository nearByPlaces;
    AutocompleteRepository autoCompleteRepository;
    DetailsRepository detailRestaurantRepository;
    public static MutableLiveData<List<Result>> lRestaurantMutableLiveData = new MutableLiveData<>();
    public static MutableLiveData<List<User>> lWorkmatesLiveData = new MutableLiveData<>();

    public static MutableLiveData<PlaceDetailsResult> dRestaurant = new MutableLiveData<>();

    @NonNull
    private MutableLiveData<ArrayList<Restaurant>> lRestaurantStateItemLiveData = new MutableLiveData<>();

    @NonNull
    private final Permissions permissionChecker;

    @NonNull
    private static LocationRepository locationRepository;

    @NonNull
    private final LiveData<Location> gpsMessageLiveData;

    UserRepository userRepository;

    public RestaurantsViewModel(
            @NonNull Permissions permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull PlacesRepository nearByPlaces,
            @NonNull UserRepository userRepository,
            AutocompleteRepository autoCompleteRepository,
            DetailsRepository detailRestaurantRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.nearByPlaces = nearByPlaces;
        this.userRepository = userRepository;
        this.autoCompleteRepository = autoCompleteRepository;
         this.detailRestaurantRepository = detailRestaurantRepository;

        getLocation();

        gpsMessageLiveData = Transformations.map(locationRepository.getLocationLiveData(), location -> location);
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        if (!permissionChecker.hasLocationPermission()) {
            locationRepository.stopLocationRequest();
        } else {
            locationRepository.getLocationRequest();
        }
    }

    public LiveData<Location> getGpsMessageLiveData() {
        return Objects.requireNonNull(gpsMessageLiveData);
    }

    public void initListViewMutableLiveData(String userLocationStr) {
        lRestaurantMutableLiveData = nearByPlaces.getNearbyPlaces(userLocationStr);
    }

    private LiveData<List<Restaurant>> mapDataToViewState
    (MutableLiveData<List<Result>> lRestaurantMutableLiveData, List<User> listWorkmates) {
        return Transformations.map(lRestaurantMutableLiveData, restaurant -> {
            ArrayList<Restaurant> listRestaurantStateItems = new ArrayList<>();
            lRestaurantStateItemLiveData = new MutableLiveData<>();
            for (Result result : restaurant) {
                listRestaurantStateItems.add(new Restaurant(
                        result.getPlaceId(),
                        result.getName().toLowerCase(),
                        result.getRating(),
                        openOrNot(result.getOpenNow()),
                        result.getVicinity(),
                        result.getGeometry().getLocation(),
                        (int) Math.round(calculateDistance(result.getGeometry().getLocation())),
                        result.getPhotos(),
                calculateNbWorkmates(result.getPlaceId(), listWorkmates)));

            }
            lRestaurantStateItemLiveData.setValue(listRestaurantStateItems);
            return listRestaurantStateItems;
        });
    }

    public LiveData<List<Restaurant>> getListRestaurants() {
        List<User> lWorkmates = userRepository.getAllUsers().getValue();
        return mapDataToViewState(lRestaurantMutableLiveData, lWorkmates);
    }

    private Double calculateDistance(Location restaurantLocation) {
        double distance = 0;
        Location rLocation = new Location(restaurantLocation.getLat(), restaurantLocation.getLng());
        Location userLocation = locationRepository.getLocationLiveData().getValue();

        distance = Location.computeDistance(userLocation, rLocation);
        return distance;
    }

    public Double distance(Location oldLocation, Location newLocation) {
        return Location.computeDistance(oldLocation, newLocation);
    }
    private Boolean openOrNot(OpeningHours openingNow) {
        Boolean status;
        if (openingNow != null) {
            status = openingNow.getOpenNow();
        } else {
            status = false;
        }
        return status;
    }

    private Integer calculateNbWorkmates(String placeId, List<User> listWorkmates) {
        int nbWorkmates = 0;
        for (User result : listWorkmates) {
            if ((result.getChosenRestaurantId() != null) && (result.getChosenRestaurantId().equals(placeId))) {
                nbWorkmates++;
            }
        }
        return nbWorkmates;
    }




    public Location getLocationForReinitMap() {
        return locationRepository.getLocationLiveData().getValue();
    }

    public void getAutocomplete(String placeId) {
        autoCompleteRepository.getAutocomplete(placeId);
    }

    public MutableLiveData<List<Prediction>> getListRestaurantAutoComplete() {
        return autoCompleteRepository.getListAutoComplete();
    }

    public void callPlaces(String placeId) {
        dRestaurant = detailRestaurantRepository.getPlaceDetails(placeId);

    }

    public LiveData<ArrayList<Result>> getDetailRestaurant() {
        detailRestaurantRepository.getRestaurantDetails();
        return mapDataToResultAPIMap(dRestaurant);
    }

    private LiveData<ArrayList<Result>> mapDataToResultAPIMap(LiveData<PlaceDetailsResult> dRestaurant) {
        return Transformations.map(dRestaurant, detailRestaurant -> {
            ArrayList<Result> lRestauDetail = new ArrayList();
            Result restauDetail = new Result();
            Boolean openNow = detailRestaurant.getOpening_hours().getOpenNow();
            OpeningHours open_Now = new OpeningHours();
            open_Now.setOpenNow(openNow);
            Geometry geometryApiMapRestau = new Geometry();
            geometryApiMapRestau.setLocation(detailRestaurant.getGeometry().getLocation());
            restauDetail.setPlaceId(detailRestaurant.getPlaceId());
            restauDetail.setName(detailRestaurant.getName());
            restauDetail.setVicinity(detailRestaurant.getVicinity());
            restauDetail.setRating(detailRestaurant.getRating());
            restauDetail.setPhotos(detailRestaurant.getPhotos());
            restauDetail.setOpen_now(open_Now);
            restauDetail.setGeometry(geometryApiMapRestau);
            lRestauDetail.add(restauDetail);
            lRestaurantMutableLiveData.setValue(lRestauDetail);
            return lRestauDetail;
        });
    }

}


