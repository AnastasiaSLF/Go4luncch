package com.example.go4luncch;

import com.example.go4luncch.NearbySearch.Geometry;
import com.example.go4luncch.NearbySearch.Location;
import com.example.go4luncch.NearbySearch.OpeningHours;
import com.example.go4luncch.NearbySearch.Photo;
import com.example.go4luncch.NearbySearch.Result;
import com.example.go4luncch.models.Restaurant;
import com.example.go4luncch.models.RestaurantLiked;
import com.example.go4luncch.models.User;

import java.util.ArrayList;
import java.util.List;

public class DataTest {

    private static final String sRestaurantNameTest = "Restaurant name test";
    private static final String sRestaurantAddressTest = "Restaurant address test";
    private static final boolean sRestaurantOpeningTest = true;
    private static final double sRestaurantRatingTest = 3;
    private static final String sPhoneNumberTest = "0123456789";
    private static final String sRestaurantWebsiteTest = "www.restaurant.com";

    public static Location generateCurrentPositionTest() {
        Location userLocation = new Location();
        userLocation.setLat(48.5244883);
        userLocation.setLng(2.3842167);
        return userLocation;
    }

    public static List<User> generateCoworkerTest() {
        List<User> fakeCoworker = new ArrayList<>();
        User laurence = new User(
                "Laurence Id",
                "Laurence Name",
                "laurencemail@orange.fr",
                "https://i.pravatar.cc/150?u=a042581f4e29026704d",
                "Restaurant 1",
                "Restaurant 1 name",
                "address Restaurant 1",
                "04/03/2022");
        fakeCoworker.add(laurence);

        User thierry = new User(
                "Thierry Id",
                "Thierry Name",
                "thierrymail@orange.fr",
                "https://i.pravatar.cc/150?u=a042581f4e29026704e",
                "Chosen Restaurant Id Thierry",
                "Chosen Restaurant Name Thierry",
                "Chosen Restaurant Address Thierry",
                "04/03/2022");
        fakeCoworker.add(thierry);

        User marie = new User(
                "Marie Id",
                "Marie Name",
                "mariemail@orange.fr",
                "https://i.pravatar.cc/150?u=a042581f4e29026704f",
                "Chosen Restaurant Id Marie",
                "Chosen Restaurant Name Marie",
                "Chosen Restaurant Address Marie",
                "04/03/2022");
        fakeCoworker.add(marie);

        return fakeCoworker;
    }

    public static String getRestaurantNameTest() {
        return sRestaurantNameTest;
    }

    public static String getRestaurantAddressTest() {
        return sRestaurantAddressTest;
    }

    public static boolean getRestaurantOpeningTest() {
        return sRestaurantOpeningTest;
    }

    public static float getRestaurantRatingTest() {
        return (float) sRestaurantRatingTest;
    }

    public static String getRestaurantWebsiteTest() {
        return sRestaurantWebsiteTest;
    }

    public static String getPhoneNumberTest() {
        return sPhoneNumberTest;
    }

    public static List<RestaurantLiked> getFavoriteRestaurantTest() {
        List<RestaurantLiked> favoriteRestaurantList = new ArrayList<>();
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 1", "Marie Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 1", "Thierry Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 2", "Laurence Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 3", "Laurence Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 4", "Marie Id"));
        favoriteRestaurantList.add(new RestaurantLiked("Restaurant 5", "Laurence"));

        return favoriteRestaurantList;
    }

    public static List<Restaurant> generateListRestaurantTest() {
        List<Restaurant> lRestaurantStateItem = new ArrayList<>();
        lRestaurantStateItem.add(new Restaurant("Restaurant 1",
                "Restaurant 1 name",
                3F,
                true,
                "address Restaurant 1",
                getLocationRestaurant1(),
                24,
                getPhotosAttributes1(),
                0));
        lRestaurantStateItem.add(new Restaurant("Restaurant 2",
                "Restaurant 2 name",
                1F,
                false,
                "address Restaurant 2",
                getLocationRestaurant2(),
                31,
                getPhotosAttributes1(),
                5));

        lRestaurantStateItem.add(new Restaurant("Restaurant 3",
                "Restaurant 3 name",
                5F,
                true,
                "address Restaurant 3",
                getLocationRestaurant3(),
                44,
                getPhotosAttributes1(),
                0));

        lRestaurantStateItem.add(new Restaurant("Restaurant 4",
                "Restaurant 4 name",
                1F,
                true,
                "address Restaurant 4",
                getLocationRestaurant4(),
                54,
                getPhotosAttributes1(),
                9));
        return lRestaurantStateItem;
    }

    private static Location getLocationRestaurant1() {
        Location location1 = new Location();
        location1.setLat(48.5242751);
        location1.setLng(2.3842851);
        return location1;
    }

    private static List<Photo> getPhotosAttributes1() {
        ArrayList<Photo> lPhoto1 = new ArrayList<>();
        Photo photo1 = new Photo();
        photo1.setPhotoReference("Photo Restaurant");
        photo1.setHeight(360);
        photo1.setWidth(3000);
        photo1.setHtmlAttributions(null);
        lPhoto1.add(photo1);
        return lPhoto1;
    }


    private static Location getLocationRestaurant2() {
        Location location1 = new Location();
        location1.setLat(48.52426);
        location1.setLng(2.3844617);
        return location1;
    }
    private static Location getLocationRestaurant3() {
        Location location1 = new Location();
        location1.setLat(48.5240897);
        location1.setLng(2.3841729);
        return location1;
    }
    private static Location getLocationRestaurant4() {
        Location location1 = new Location();
        location1.setLat(48.5244272);
        location1.setLng(2.383495);
        return location1;
    }

    public static List<Result> generateListRestaurantAPITest() {
        ArrayList<Result> lRestaurantNearByPlace = new ArrayList();
        lRestaurantNearByPlace.add(new Result("Restaurant 1",
                "Restaurant 1 name",
                3F,
                getOpenNow(),
                "address Restaurant 1",
                getGeometryRestaurant1(),
                getPhotosAttributes1(),
                getListType()
        ));

        lRestaurantNearByPlace.add(new Result("Restaurant 2",
                "Restaurant 2 name",
                1F,
                getOpenNow(),
                "address Restaurant 2",
                getGeometryRestaurant2(),
                getPhotosAttributes1(),
                getListType()
        ));
        lRestaurantNearByPlace.add(new Result("Restaurant 3",
                "Restaurant 3 name",
                5F,
                getOpenNow(),
                "address Restaurant 3",
                getGeometryRestaurant3(),
                getPhotosAttributes1(),
                getListType()
        ));
        lRestaurantNearByPlace.add(new Result("Restaurant 4",
                "Restaurant 4 name",
                1F,
                getOpenNow(),
                "address Restaurant 4",
                getGeometryRestaurant1(),
                getPhotosAttributes1(),
                getListType()
        ));

        return lRestaurantNearByPlace;
    }

    private static Geometry getGeometryRestaurant1() {
        Geometry geometryAPIMap1 = new Geometry();
        geometryAPIMap1.setLocation(getLocationRestaurant1());
        geometryAPIMap1.setViewport(null);
        return geometryAPIMap1;
    }

    private static Geometry getGeometryRestaurant2() {
        Geometry geometryAPIMap1 = new Geometry();
        geometryAPIMap1.setLocation(getLocationRestaurant2());
        geometryAPIMap1.setViewport(null);
        return geometryAPIMap1;
    }
    private static Geometry getGeometryRestaurant3() {
        Geometry geometryAPIMap1 = new Geometry();
        geometryAPIMap1.setLocation(getLocationRestaurant3());
        geometryAPIMap1.setViewport(null);
        return geometryAPIMap1;
    }

    private static OpeningHours getOpenNow() {
        OpeningHours openStatus = new OpeningHours();
        openStatus.setOpenNow(true);
        return openStatus;
    }
    private static ArrayList<String> getListType() {
        ArrayList<String> lType = new ArrayList<>();
        lType.add("restaurant");
        return lType;

    }


}

