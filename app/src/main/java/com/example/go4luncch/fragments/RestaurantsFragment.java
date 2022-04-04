package com.example.go4luncch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4luncch.R;
import com.example.go4luncch.ViewModelFactory;
import com.example.go4luncch.adapters.RestaurantAdapter;
import com.example.go4luncch.models.Restaurant;
import com.example.go4luncch.viewmodels.RestaurantsViewModel;

import java.util.List;

public class RestaurantsFragment  extends Fragment {
    private RecyclerView recyclerView;
    private RestaurantsViewModel listRestaurantViewModel;
    public static final int RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;

    private ProgressBar progressBar;
    private TextView noRestaurants;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listRestaurantViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance()).get(RestaurantsViewModel.class);

        noRestaurants = view.findViewById(R.id.restaurant_List_no_restaurants_to_show);
        progressBar = view.findViewById(R.id.restaurants_progress_bar);

        recyclerView = view.findViewById(R.id.restaurants_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        progressBar.setVisibility(View.VISIBLE);

        getRestaurants();

        return view;
    }

    private void getRestaurants() {
        listRestaurantViewModel.getListRestaurants().observe(
                requireActivity(),
                changedListRestaurantsStateItem -> {
                    if (changedListRestaurantsStateItem != null) {
                        sendResultsToAdapter(changedListRestaurantsStateItem);
                    } else {
                        noRestaurantsToShow();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    private void noRestaurantsToShow() {
        progressBar.setVisibility(View.GONE);
        noRestaurants.setText(R.string.restaurant_list_no_restaurants_to_show);
        noRestaurants.setVisibility(View.VISIBLE);
    }

    private void sendResultsToAdapter(List<Restaurant> results) {
        if (results == null || results.isEmpty()) {
            noRestaurantsToShow();
        } else {
            recyclerView.setAdapter(new RestaurantAdapter(results, this.getActivity()));
        }
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}

