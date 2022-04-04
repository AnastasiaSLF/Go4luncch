package com.example.go4luncch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4luncch.R;
import com.example.go4luncch.models.Restaurant;
import com.example.go4luncch.utils.Details;
import com.example.go4luncch.view.RestaurantsViewHolder;

import java.util.List;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    private List<Restaurant> results;
    private final FragmentActivity activity;

    public RestaurantAdapter(List<Restaurant> results, FragmentActivity activity) {
        this.results = results;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_restaurant;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        view.getContext();
        return new RestaurantsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v ->
                Details.openDetailsFragment(
                        activity,
                        results.get(position).getPlaceId()));
        holder.createViewWithRestaurants(results.get(position));
    }


    @Override
    public int getItemCount() {
        return results.size();
    }
}
