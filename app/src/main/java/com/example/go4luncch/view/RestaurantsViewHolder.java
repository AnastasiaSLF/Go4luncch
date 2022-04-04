package com.example.go4luncch.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4luncch.NearbySearch.Photo;
import com.example.go4luncch.R;
import com.example.go4luncch.models.Restaurant;
import com.example.go4luncch.utils.Ratings;

import java.text.MessageFormat;

public class RestaurantsViewHolder extends RecyclerView.ViewHolder {
    private final TextView rName = itemView.findViewById(R.id.item_restaurant_name);
    private final TextView rAddress = itemView.findViewById(R.id.item_restaurant_address);
    private final TextView rDistance = itemView.findViewById(R.id.item_restaurant_distance);
    private final TextView rStatusOpening = itemView.findViewById(R.id.item_restaurant_hours);
    private final TextView rWorkmates = itemView.findViewById(R.id.item_restaurant_text_number_workmates);
    private final ImageView rPicture = itemView.findViewById(R.id.item_restaurant_picture);
    private final ImageView star1 = itemView.findViewById(R.id.item_restaurant_rating_star1);
    private final ImageView star2 = itemView.findViewById(R.id.item_restaurant_rating_star2);
    private final ImageView star3 = itemView.findViewById(R.id.item_restaurant_rating_star3);


    public RestaurantsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void createViewWithRestaurants(Restaurant result) {
        rName.setText(String.valueOf(result.getName()));
        rAddress.setText(String.valueOf(result.getVicinity()));
        rDistance.setText(MessageFormat.format("{0}{1}", result.getRestaurantDistance(), "m"));
        if (result.getOpenNow()) {
            rStatusOpening.setText(R.string.item_statut_open);
        } else {
            rStatusOpening.setText(R.string.item_statut_close);
        }
        Ratings.showRating(result.getRating(), star1, star2, star3);
        if (result.getNbWorkmates() > 0) {
            rWorkmates.setText(String.valueOf(result.getNbWorkmates()));
        } else {
            rWorkmates.setVisibility(View.INVISIBLE);
        }

        Ratings.showRating(result.getRating(), star1, star2, star3);
        showPicture(result);
    }


    private void showPicture(Restaurant result) {
        Context context = itemView.getContext();
        String API_KEY = context.getResources().getString(R.string.maps_api_key);

        if (result.getPhotos() == null) {
            rPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = itemView.findViewById(R.id.item_restaurant_no_picture_text);
            noPhoto.setText(R.string.no_picture);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
            Photo photoAttributesAPIMap = result.getPhotos().get(0);

            Glide.with(this.itemView.getContext())
                    .load(photoAttributesAPIMap.getPhoto_URL() + API_KEY)
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(rPicture);
        }
    }
}
