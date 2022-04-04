package com.example.go4luncch.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.go4luncch.R;
import com.example.go4luncch.fragments.DetailsFragment;

public class Details {
    public static DetailsFragment newInstance(String placeId) {
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        DetailsFragment f = new DetailsFragment();
        f.setArguments(args);
        return f;
    }

    public static void openDetailsFragment(FragmentActivity activity, String placeId) {
        Fragment fragment = newInstance(placeId);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
