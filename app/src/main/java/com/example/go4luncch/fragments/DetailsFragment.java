package com.example.go4luncch.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4luncch.BuildConfig;
import com.example.go4luncch.R;
import com.example.go4luncch.ViewModelFactory;
import com.example.go4luncch.adapters.WorkmatesAdapter;
import com.example.go4luncch.models.DetailsRestaurant;
import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.UserSingletonRepository;
import com.example.go4luncch.utils.Ratings;
import com.example.go4luncch.viewmodels.DetailsViewmodel;

import java.util.List;
import java.util.Objects;

public class DetailsFragment extends Fragment {
    public static final String TAG = "DetailsFragment";

    public String placeId;
    private DetailsViewmodel detailRestaurantViewModel;

    private TextView restaurantName;
    private ImageView restaurantPicture;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageButton buttonBack;
    private ImageButton buttonRestaurantChoice;
    private Button buttonLike;

    private Button buttonPhone;
    private Button buttonWebsite;
    private RecyclerView recyclerView;

    private String chosenRestaurantId;
    String chosenRestaurantName;
    String chosenRestaurantAdress;

    public DetailsFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        placeId = requireArguments().getString("placeId");
        detailRestaurantViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DetailsViewmodel.class);
        detailRestaurantViewModel.initDetailViewMutableLiveData(placeId);

        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        getDetailRestaurant(view);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDetailRestaurant(View view) {
        detailRestaurantViewModel.getMediatorLiveData().observe(
                getViewLifecycleOwner(),
                detailRestaurantStateItem -> {
                    hideActivityViews();
                    configureView(view, detailRestaurantStateItem);
                    setPicture(view, detailRestaurantStateItem);
                    restaurantChoiceLayout(detailRestaurantStateItem);
                    restaurantLikeLayout(detailRestaurantStateItem);
                    populateWorkmates(detailRestaurantStateItem.getPlaceId());
                    Ratings.showRating(detailRestaurantStateItem.getRating(), star1, star2, star3);
                    setClickableFunctionality(detailRestaurantStateItem);
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void configureView(View view, DetailsRestaurant detailRestaurant) {
        restaurantName = view.findViewById(R.id.restaurant_details_name);
        TextView restaurantAdress = view.findViewById(R.id.restaurant_details_address);
        restaurantPicture = view.findViewById(R.id.restaurant_details_picture);
        buttonPhone = view.findViewById(R.id.restaurant_details_phone_call);
        buttonLike = view.findViewById(R.id.restaurant_details_like);
        buttonWebsite = view.findViewById(R.id.restaurant_details_website);
        buttonBack = view.findViewById(R.id.fragment_restaurant_details_button_backpress);
        buttonRestaurantChoice = view.findViewById(R.id.fragment_restaurant_details_button_restaurant_choice);

        buttonPhone.setText(R.string.restaurant_details_phone_call);
        buttonLike.setText("LIKE");
        buttonWebsite.setText(R.string.restaurant_details_website);

        restaurantName.setText(detailRestaurant.getName());
        restaurantAdress.setText(detailRestaurant.getFormatted_address());

        star1 = view.findViewById(R.id.restaurant_details_star1);
        star2 = view.findViewById(R.id.restaurant_details_star2);
        star3 = view.findViewById(R.id.restaurant_details_star3);
        recyclerView = view.findViewById(R.id.restaurant_details_workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setPicture(View view, DetailsRestaurant detailRestaurant) {
        if (detailRestaurant.getPhotos() == null) {
            restaurantPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = view.findViewById(R.id.restaurant_details_no_picture_text);
            noPhoto.setText(R.string.no_picture);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
            Glide.with(view)
                    .load(detailRestaurant.getPhotos().get(0).getPhoto_URL() + BuildConfig.MAPS_API_KEY)
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(restaurantPicture);
            restaurantPicture.setVisibility(View.VISIBLE);
            TextView noPhoto = view.findViewById(R.id.restaurant_details_no_picture_text);
            noPhoto.setText("");
            noPhoto.setVisibility(View.GONE);
        }
    }

    private void restaurantChoiceLayout(DetailsRestaurant detailRestaurant) {
        chosenRestaurantId = detailRestaurant.getChoosenRestaurantId();
        chosenRestaurantName = detailRestaurant.getChoosenRestaurantName();
        chosenRestaurantAdress = detailRestaurant.getChoosenRestaurantAdress();
        if (chosenRestaurantId != null && chosenRestaurantId.equals(placeId)) {
            buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_24);
        } else {
            buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_add_24);
        }
    }


    private void restaurantLikeLayout(DetailsRestaurant detailRestaurant ) {
        if (detailRestaurant.getUserLike() != null && detailRestaurant.getUserLike()) {
            buttonLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24, 0, 0);
        } else {
            buttonLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_border_24, 0, 0);
        }
    }

    private void populateWorkmates(String placeId) {
        detailRestaurantViewModel.getWorkmatesForARestaurant(placeId).observe(
                requireActivity(),
                changedListWorkmates -> {
                    if (changedListWorkmates != null) {
                        sendResultsToAdapter(changedListWorkmates);
                    }
                });
    }

    private void sendResultsToAdapter(List<User> lWorkmatesExceptCurrentUser) {
        recyclerView.setAdapter(new WorkmatesAdapter(lWorkmatesExceptCurrentUser, this.getActivity(), TAG));
        recyclerView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setClickableFunctionality(DetailsRestaurant detailRestaurant) {
        buttonPhone.setOnClickListener(v -> {
            if (detailRestaurant.getInternationalPhoneNumber() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_phone_number, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + detailRestaurant.getInternationalPhoneNumber().trim()));
                startActivity(intent);
            }
        });

        buttonWebsite.setOnClickListener(v -> {
            if (detailRestaurant.getWebsite() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_website, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(detailRestaurant.getWebsite()));
                startActivity(openURL);
            }
        });

        buttonLike.setOnClickListener(v -> buttonLikeResponse(detailRestaurant));

        buttonBack.setOnClickListener(v -> requireActivity().onBackPressed());

        buttonRestaurantChoice.setOnClickListener(v -> buttonRestaurantChoiceResponse(detailRestaurant));
    }

    private void buttonLikeResponse(DetailsRestaurant detailRestaurantStateItem) {
        if (detailRestaurantStateItem.getUserLike()!= null && detailRestaurantStateItem.getUserLike()) {
            detailRestaurantViewModel.deleteRestaurantLiked(detailRestaurantStateItem.getPlaceId());
        } else {
            detailRestaurantViewModel.addRestaurantLiked(detailRestaurantStateItem.getPlaceId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buttonRestaurantChoiceResponse(DetailsRestaurant detailRestaurantStateItem) {
        User mUser;
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        mUser = userSingletonRepository.getUser();
        if (mUser.getChosenRestaurantId() == null || mUser.getChosenRestaurantId().equals("")) {
            saveRestaurantChoice(detailRestaurantStateItem.getPlaceId(), detailRestaurantStateItem.getName(),
                    detailRestaurantStateItem.getFormatted_address());
            buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_24);
            Toast.makeText(getContext(), getString(R.string.restaurant_Chosen, restaurantName.getText()), Toast.LENGTH_SHORT).show();
        } else
            if (chosenRestaurantId.equals(detailRestaurantStateItem.getPlaceId())) {
                alertRestaurantCancel();
            } else
                if (!chosenRestaurantId.equals(detailRestaurantStateItem.getPlaceId())) {
                    alertRestaurantChange(detailRestaurantStateItem);
                }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void alertRestaurantCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_message)
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_title);
        AlertDialog dialogRestaurantChosen = builder.create();
        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
            saveRestaurantChoice(null,
                    null, null);
            buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_add_24);

        });

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("StringFormatInvalid")
    private void alertRestaurantChange(DetailsRestaurant detailRestaurantStateItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(requireContext().getString(R.string.details_fragment_alertdialog_restaurant_choice_change_message, detailRestaurantStateItem.getName()))
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_change_title);

        AlertDialog dialogRestaurantChosen = builder.create();

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES),
                (dialog1, which) -> {
                    saveRestaurantChoice(detailRestaurantStateItem.getPlaceId(), detailRestaurantStateItem.getName(),
                            detailRestaurantStateItem.getFormatted_address());
                    buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_24);
                });
        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveRestaurantChoice(String chosenRestaurantId, String chosenRestaurantName, String chosenRestaurantAdress) {
        detailRestaurantViewModel.updateRestaurantChoice(requireContext(), chosenRestaurantId,
                chosenRestaurantName, chosenRestaurantAdress);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideActivityViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        showActivityViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showActivityViews();
    }

    private void hideActivityViews() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        requireActivity().findViewById(R.id.navbar).setVisibility(View.INVISIBLE);
    }

    private void showActivityViews() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        requireActivity().findViewById(R.id.navbar).setVisibility(View.VISIBLE);
    }
}
