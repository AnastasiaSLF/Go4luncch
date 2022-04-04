package com.example.go4luncch.fragments;

import static com.example.go4luncch.MainActivity.RADIUS_INIT;
import static com.example.go4luncch.MainActivity.RADIUS_MAX;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.go4luncch.R;
import com.example.go4luncch.ViewModelFactory;
import com.example.go4luncch.viewmodels.SettingViewModel;
import com.google.android.material.slider.Slider;

import java.util.Objects;


public class SettingFragment extends Fragment {
    private TextView textRadius;
    private Slider sliderRadius;
    private ImageButton buttonBackPress;
    private SwitchCompat switchNotifications;
    private SettingViewModel settingViewModel;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        settingViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(SettingViewModel.class);
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        configureView(view);
        setClickHandler(view);
        radiusSliderListener(view);
        backPressHandler();
        return view;
    }

    private void setClickHandler(View view) {
        switchNotifications.setOnCheckedChangeListener((cb, notificationChoice) -> settingViewModel.saveNotifications(view.getContext(), notificationChoice));
    }

    private void radiusSliderListener(View view) {
        sliderRadius.addOnChangeListener((slider, value, fromUser) -> {
            RADIUS_INIT = (int) value;
            textRadius.setText(getString(R.string.fragment_settings_radius_search, RADIUS_INIT));
            settingViewModel.saveRadius(view.getContext(), value);
        });
    }

    private void backPressHandler() {
        buttonBackPress.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void configureView(View view) {
        TextView title = view.findViewById(R.id.fragment_settings_title);
        textRadius = view.findViewById(R.id.fragment_settings_radius_text);
        buttonBackPress = view.findViewById(R.id.fragment_settings_backpress);
        switchNotifications = view.findViewById(R.id.fragment_settings_update_notifications_switch);
        sliderRadius = view.findViewById(R.id.fragment_settings_radius_slider);

        int CHOOSEN_RADIUS = Math.round(settingViewModel.getRadius(requireContext()));
        title.setText(R.string.fragment_settings_title);
        textRadius.setText(getString(R.string.fragment_settings_radius_search, CHOOSEN_RADIUS));

        sliderRadius.setValue(CHOOSEN_RADIUS);
        sliderRadius.setValueTo(RADIUS_MAX);
        int RADIUS_MIN = 1000;
        sliderRadius.setValueFrom(RADIUS_MIN);
        sliderRadius.setLabelFormatter(value -> String.valueOf(CHOOSEN_RADIUS));
        int RADIUS_STEP = 500;
        sliderRadius.setStepSize(RADIUS_STEP);

        switchNotifications.setChecked(settingViewModel.getNotifications(requireContext()));
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
