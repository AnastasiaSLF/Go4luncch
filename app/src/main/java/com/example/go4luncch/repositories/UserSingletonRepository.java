package com.example.go4luncch.repositories;

import com.example.go4luncch.models.User;

public class UserSingletonRepository {
    User mUser;

    private static final UserSingletonRepository userInstance = new UserSingletonRepository();
    public static UserSingletonRepository getInstance() {
        return userInstance;
    }

    public UserSingletonRepository() { }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public User getUser() {
        return mUser;
    }

    public void updateRestaurantChoice(String chosenRestaurantId, String chosenRestaurantName,
                                       String chosenRestaurantAddress, String chosenRestaurantDate) {
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        User userSingleton = getUser();

        userSingleton.setChosenRestaurantId(chosenRestaurantId);
        userSingleton.setChosenRestaurantName(chosenRestaurantName);
        userSingleton.setChosenRestaurantAddress(chosenRestaurantAddress);
        userSingleton.setChosenRestaurantDate(chosenRestaurantDate);
        setUser(userSingleton);




    }


}
