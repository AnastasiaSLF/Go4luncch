package com.example.go4luncch.viewmodels;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4luncch.PlaceDetails.PlaceDetailsResult;
import com.example.go4luncch.models.DetailsRestaurant;
import com.example.go4luncch.models.RestaurantChoice;
import com.example.go4luncch.models.RestaurantLiked;
import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.DetailsRepository;
import com.example.go4luncch.repositories.SharedPreferencesRepository;
import com.example.go4luncch.repositories.UserLikedRepository;
import com.example.go4luncch.repositories.UserRepository;
import com.example.go4luncch.repositories.UserSingletonRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsViewmodel extends ViewModel {

        private DetailsRepository detailRestaurantRepository;
        private UserLikedRepository userLikingRestaurantRepository;
        private UserRepository userRepository;
        private final MediatorLiveData<DetailsRestaurant> mMediatorLiveData = new MediatorLiveData<>();

    public DetailsViewmodel(
                DetailsRepository detailRestaurantRepository,
                UserRepository userRepository,
                UserLikedRepository userLikingRestaurantRepository) {
            this.detailRestaurantRepository = detailRestaurantRepository;
            this.userRepository = userRepository;
            this.userLikingRestaurantRepository = userLikingRestaurantRepository;

            LiveData<PlaceDetailsResult> restaurantDetails = detailRestaurantRepository.getRestaurantDetails();
            LiveData<List<RestaurantLiked>> usersLikingRestaurant = userLikingRestaurantRepository.getUsersLikingRestaurant();
            MutableLiveData<ArrayList<User>> currentUserLiveData = userRepository.getFirestoreUser();

            mMediatorLiveData.addSource(restaurantDetails, restaurantDetails1 -> combine(restaurantDetails1,
                    usersLikingRestaurant.getValue(),
                    currentUserLiveData.getValue()));

            mMediatorLiveData.addSource(currentUserLiveData, currentUserLiveData1 -> combine(restaurantDetails.getValue(),
                    usersLikingRestaurant.getValue(),
                    currentUserLiveData1));

            mMediatorLiveData.addSource(usersLikingRestaurant, restaurantLiked -> combine(restaurantDetails.getValue(),
                    restaurantLiked,
                    currentUserLiveData.getValue()));
        }

        private void combine(@NonNull PlaceDetailsResult restaurantDetail,
                @Nullable List<RestaurantLiked> likingRestaurant,
                @NonNull ArrayList<User> currentUserLiveData) {
            /**
             * Main method. all the parameters allow to create a model which will dispense the views
             * At the end a view state is created with basic fields for display correct data
             * @param restaurantDetail get the information from Places API
             * @param User liking a restaurant
             */
            if (restaurantDetail != null) {
                DetailsRestaurant restaurant = new DetailsRestaurant(
                        restaurantDetail.getPlaceId(),
                        restaurantDetail.getName(),
                        restaurantDetail.getOpening_hours(),
                        restaurantDetail.getRating(),
                        restaurantDetail.getWebsite(),
                        restaurantDetail.getInternational_phone_number(),
                        restaurantDetail.getFormatted_address(),
                        restaurantDetail.getPhotos(),
                        false,
                        null,
                        null,
                        null,
                        null);

                if (currentUserLiveData != null) {
                    restaurant.setChoosenRestaurantId(currentUserLiveData.get(0).getChosenRestaurantId());
                    restaurant.setChoosenRestaurantName(currentUserLiveData.get(0).getChosenRestaurantName());
                    restaurant.setChoosenRestaurantAdress(currentUserLiveData.get(0).getChosenRestaurantAddress());
                }
                if (likingRestaurant != null) {
                    restaurant.setUserLike(userLikingRestaurantRepository.getUserLike(restaurant.getPlaceId()));
                }

                mMediatorLiveData.setValue(restaurant);
            }
        }

        //LiveData observed by the View
        public LiveData<DetailsRestaurant> getMediatorLiveData() {
            return mMediatorLiveData;
        }

        // fin mise en place Mediator LiveData
        public MutableLiveData<List<User>> getWorkmatesForARestaurant(String placeId) {
            return userRepository.getAllUsersForARestaurant(placeId);
        }

        public void initDetailViewMutableLiveData(String placeId) {
            detailRestaurantRepository.getPlaceDetails(placeId);
        }

        public void addRestaurantLiked(String placeId) {
            userLikingRestaurantRepository.addRestaurantLiked(placeId);
        }

        public void deleteRestaurantLiked(String placeId) {
            userLikingRestaurantRepository.deleteRestaurantLiked(placeId);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void updateRestaurantChoice(Context context, String chosenRestaurantId,
                String chosenRestaurantName, String chosenRestaurantAdress) {
            UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
            User mUser = userSingletonRepository.getUser();
            mUser.setChosenRestaurantId(chosenRestaurantId);
            mUser.setChosenRestaurantName(chosenRestaurantName);
            mUser.setChosenRestaurantAddress(chosenRestaurantAdress);

            Date now = new Date();
            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
            String formattedDate = dateFormatter.format(now);
            if (chosenRestaurantId != null) {
                mUser.setChosenRestaurantDate(formattedDate);
            } else {
                mUser.setChosenRestaurantDate(null);
            }
            userRepository.updateUserFirestore(mUser);

            userSingletonRepository.updateRestaurantChoice(chosenRestaurantId, chosenRestaurantName,
                    chosenRestaurantAdress, formattedDate);

            SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepository();
            RestaurantChoice restaurantChoice = new RestaurantChoice();
            restaurantChoice.setUserId(mUser.getUid());
            restaurantChoice.setChosenRestaurantAddress(mUser.getChosenRestaurantAddress());
            restaurantChoice.setChosenRestaurantName(mUser.getChosenRestaurantName());
            restaurantChoice.setChosenDate(mUser.getChosenRestaurantDate());
            sharedPreferencesRepository.saveRestaurantChoice(context, restaurantChoice);
        }
    }
