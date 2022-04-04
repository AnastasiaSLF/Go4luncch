package com.example.go4luncch;
import static com.example.go4luncch.viewmodels.RestaurantsViewModel.lRestaurantMutableLiveData;

import static com.example.go4luncch.viewmodels.RestaurantsViewModel.lWorkmatesLiveData;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4luncch.NearbySearch.Geometry;
import com.example.go4luncch.NearbySearch.Location;
import com.example.go4luncch.NearbySearch.Result;
import com.example.go4luncch.models.Restaurant;
import com.example.go4luncch.repositories.AutocompleteRepository;
import com.example.go4luncch.repositories.DetailsRepository;
import com.example.go4luncch.repositories.LocationRepository;
import com.example.go4luncch.repositories.PlacesRepository;
import com.example.go4luncch.repositories.UserRepository;
import com.example.go4luncch.utils.Permissions;
import com.example.go4luncch.viewmodels.RestaurantsViewModel;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)

public class RestaurantViewModelUnitTest {
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application mApplication = BaseApplication.getApplication();
    private final Permissions permissionChecker = Mockito.mock(Permissions.class);
    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final PlacesRepository nearByPlacesRepository = Mockito.mock(PlacesRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AutocompleteRepository autoCompleteRepository = Mockito.mock(AutocompleteRepository.class);
    private final DetailsRepository detailRestaurantRepository = Mockito.mock(DetailsRepository.class);

    private final Result mResultAPIMap = Mockito.mock(Result.class);
    private final Result mResultsAPIMap = Mockito.mock(Result.class);
    private final Location location = Mockito.mock(Location.class);
    private final Geometry mNearbyGeometryAPIMap = Mockito.mock(Geometry.class);
    private final Location mNearbyLocationAPIMap = Mockito.mock(Location.class);

    private final MutableLiveData<Location> userLocationLiveDataTest = new MutableLiveData<>();
    private final MutableLiveData<LatLng> mCurrentPositionMutableLiveDataTest = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> lRestaurantLiveDataStateItemTest = new MutableLiveData<>();
    private final MutableLiveData<List<Result>> lRestaurantLiveDataTest = new MutableLiveData<>();

    private RestaurantsViewModel listRestaurantViewModel;

    @Before
    public void setUp() {
        List<Result> listOfNearbyResult = new ArrayList<>();

    }


    @Test
    public void getNearByPlaces() throws InterruptedException {
        lRestaurantLiveDataTest.setValue(DataTest.generateListRestaurantAPITest());

        Mockito.when(nearByPlacesRepository.getNearbyPlaces("48.5244883,2.3842167"))
                .thenReturn(lRestaurantLiveDataTest);

        List<Result> listResultAPIMap = LiveDataTest
                .getValue(nearByPlacesRepository.getNearbyPlaces("48.5244883,2.3842167"));

        Assert.assertEquals("Restaurant 1 name", listResultAPIMap.get(0).getName());
        Assert.assertEquals("Restaurant 1", listResultAPIMap.get(0).getPlaceId());
        Assert.assertEquals(3.0, listResultAPIMap.get(0).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(0).getOpenNow().getOpenNow());
        Assert.assertEquals(48.5242751, listResultAPIMap.get(0).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3842851, listResultAPIMap.get(0).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(0).getPhotos().get(0).getPhotoReference());
        Assert.assertEquals("address Restaurant 1", listResultAPIMap.get(0).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(0).getTypes().get(0));

        Assert.assertEquals("Restaurant 2 name", listResultAPIMap.get(1).getName());
        Assert.assertEquals("Restaurant 2", listResultAPIMap.get(1).getPlaceId());
        Assert.assertEquals(1.0, listResultAPIMap.get(1).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(1).getOpenNow().getOpenNow());
        Assert.assertEquals(48.52426, listResultAPIMap.get(1).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3844617, listResultAPIMap.get(1).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(1).getPhotos().get(0).getPhotoReference());
        Assert.assertEquals("address Restaurant 2", listResultAPIMap.get(1).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(1).getTypes().get(0));

        Assert.assertEquals("Restaurant 3 name", listResultAPIMap.get(2).getName());
        Assert.assertEquals("Restaurant 3", listResultAPIMap.get(2).getPlaceId());
        Assert.assertEquals(5.0, listResultAPIMap.get(2).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(2).getOpenNow().getOpenNow());
        Assert.assertEquals(48.5240897, listResultAPIMap.get(2).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3841729, listResultAPIMap.get(2).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(2).getPhotos().get(0).getPhotoReference());
        Assert.assertEquals("address Restaurant 3", listResultAPIMap.get(2).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(2).getTypes().get(0));

        Assert.assertEquals("Restaurant 4 name", listResultAPIMap.get(3).getName());
        Assert.assertEquals("Restaurant 4", listResultAPIMap.get(3).getPlaceId());
        Assert.assertEquals(1.0, listResultAPIMap.get(3).getRating(), 0);
        Assert.assertEquals(true, listResultAPIMap.get(3).getOpenNow().getOpenNow());
        Assert.assertEquals(48.5242751, listResultAPIMap.get(3).getGeometry().getLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3842851, listResultAPIMap.get(3).getGeometry().getLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , listResultAPIMap.get(3).getPhotos().get(0).getPhotoReference());
        Assert.assertEquals("address Restaurant 4", listResultAPIMap.get(3).getVicinity());
        Assert.assertEquals("restaurant", listResultAPIMap.get(3).getTypes().get(0));
    }

    @Test
    public void testUserLocation() throws InterruptedException {
        userLocationLiveDataTest.setValue(DataTest.generateCurrentPositionTest());

        Mockito.when(locationRepository.getLocationLiveData()).thenReturn(userLocationLiveDataTest);
        Location userLocation = LiveDataTest.getValue(locationRepository.getLocationLiveData());

        Assert.assertEquals(48.5244883, userLocation.getLat(),0.000001);
        Assert.assertEquals(2.3842167, userLocation.getLng(),0.00001);
    }

    @Test
    public void getInitListStateItem() throws InterruptedException {
        listRestaurantViewModel = new RestaurantsViewModel(permissionChecker, locationRepository, nearByPlacesRepository,
                userRepository, autoCompleteRepository, detailRestaurantRepository);
        lRestaurantMutableLiveData.setValue(DataTest.generateListRestaurantAPITest());
        lWorkmatesLiveData.setValue(DataTest.generateCoworkerTest());
        userLocationLiveDataTest.setValue(DataTest.generateCurrentPositionTest());

        Mockito.when(locationRepository.getLocationLiveData()).thenReturn(userLocationLiveDataTest);
        Mockito.when(userRepository.getAllUsers()).thenReturn(lWorkmatesLiveData);

        List<Restaurant> lRestaurantStateItem = LiveDataTest.getValue(listRestaurantViewModel.getListRestaurants());

        Assert.assertEquals("restaurant 1 name", lRestaurantStateItem.get(0).getName());
        Assert.assertEquals("Restaurant 1", lRestaurantStateItem.get(0).getPlaceId());
        Assert.assertEquals(3.0, lRestaurantStateItem.get(0).getRating(), 0);
        Assert.assertEquals(true, lRestaurantStateItem.get(0).getOpenNow());
        Assert.assertEquals(48.5242751, lRestaurantStateItem.get(0).getRestaurantLocation().getLat(), 0.00001);
        Assert.assertEquals(2.3842851, lRestaurantStateItem.get(0).getRestaurantLocation().getLng(), 0.00001);
        Assert.assertEquals("Photo Restaurant"
                , lRestaurantStateItem.get(0).getPhotos().get(0).getPhotoReference());
        Assert.assertEquals("adress Restaurant 1", lRestaurantStateItem.get(0).getVicinity());
        Assert.assertEquals(1, lRestaurantStateItem.get(0).getNbWorkmates(),0);
        Assert.assertEquals(24, lRestaurantStateItem.get(0).getRestaurantDistance(), 0 );
    }

}