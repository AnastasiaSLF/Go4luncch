package com.example.go4luncch.repositories;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.go4luncch.models.RestaurantChoice;

public class SharedPreferencesRepository {
    public final static String PREFS_NAME = "PREFS";
    static SharedPreferences settings;
    String userId;
    String chosenRestaurantId;
    String chosenRestaurantName;
    String chosenRestaurantAddress;
    String chosenRestaurantDate;

    public static void saveNotifications(Context context, Boolean notifications){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("NOTIFICATIONS", notifications);
        editor.apply();
    }

    public Boolean getNotifications(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getBoolean("NOTIFICATIONS", true);
    }

    public static Float getRadius(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getFloat("RADIUS", 1000);
    }

    public void saveRadius(Context context, Float radius) {
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("RADIUS", radius);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveRestaurantChoice (Context context, RestaurantChoice restaurantChoice) {
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("USERID", restaurantChoice.getUserId());
        editor.putString("CHOOSENRESTAURANTID", restaurantChoice.getChosenRestaurantId());
        editor.putString("CHOOSENRESTAURANTNAME", restaurantChoice.getChosenRestaurantName());
        editor.putString("CHOOSENRESTAURANTADRESS", restaurantChoice.getChosenRestaurantAddress());
        editor.putString("CHOOSENRESTAURANTDATE", restaurantChoice.getChosenDate());
        editor.apply();
    }



    public RestaurantChoice getRestaurantChoice(Context appContext) {
        settings = appContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        RestaurantChoice restaurantchoice = new RestaurantChoice();
        restaurantchoice.setUserId((settings.getString("USERID", userId)));
        restaurantchoice.setChosenRestaurantId(settings.getString("CHOOSENRESTAURANTID", chosenRestaurantId));
        restaurantchoice.setChosenRestaurantId(settings.getString("CHOOSENRESTAURANTID", chosenRestaurantId));
        restaurantchoice.setChosenRestaurantName(settings.getString("CHOOSENRESTAURANTNAME", chosenRestaurantName));
        restaurantchoice.setChosenRestaurantAddress(settings.getString("CHOOSENRESTAURANTADRESS", chosenRestaurantAddress));
        restaurantchoice.setChosenDate(settings.getString("CHOOSENRESTAURANTDATE", chosenRestaurantDate));
        return restaurantchoice;
    }

}

