package com.example.go4luncch.fragments;

import static com.example.go4luncch.MainActivity.RADIUS_INIT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4luncch.NearbySearch.Location;
import com.example.go4luncch.R;
import com.example.go4luncch.ViewModelFactory;
import com.example.go4luncch.models.Restaurant;
import com.example.go4luncch.viewmodels.RestaurantsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = "MapsFragment";
    private static final float INITIAL_ZOOM = 14;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float LOCATION_CHANGE_THRESHOLD = 25;

    private RestaurantsViewModel listRestaurantViewModel;

    public static Location oldUserLocation;
    public static LatLng userLatLng;
    public static String userLocation;
    public LatLng restaurantLatLng;
    public LatLng mapLatLng;

    private GoogleMap mMap;

    private SupportMapFragment mMapFragment;

    public MapsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        listRestaurantViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantsViewModel.class);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment == null) {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mMapFragment).commit();
        }

        mMapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        View mapView = mMapFragment.getView();
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0
        );

        listRestaurantViewModel.getGpsMessageLiveData().observe(getViewLifecycleOwner(),
                userLocationNew -> {
                    if (userLocationNew != null) {
                        userLatLng = new LatLng(userLocationNew.getLat(), userLocationNew.getLng());
                        if (oldUserLocation == null || (
                                listRestaurantViewModel.distance(userLocationNew, oldUserLocation)) >= LOCATION_CHANGE_THRESHOLD) {
                            oldUserLocation = userLocationNew;
                            userLocation = userLocationNew.getLat() + "," + userLocationNew.getLng();
                            listRestaurantViewModel.initListViewMutableLiveData(userLocation);
                        }
                    }
                    oldUserLocation = userLocationNew;
                    alimMap(mapView);
                }
        );
    }

    private void alimMap(View mapView) {

        enableCompassButton();
        moveCompassButton(mapView);
        getBaseList();

        mMapSetUpClickListener();
    }

    private void getBaseList() {
        listRestaurantViewModel.getListRestaurants().observe(requireActivity(),
                changedListRestaurantsStateItem -> {
                    if (changedListRestaurantsStateItem != null) {
                        if (changedListRestaurantsStateItem.isEmpty()) {
                            extendRadiusDialog();
                        } else {
                            addMarkerResult(changedListRestaurantsStateItem);
                            if (mMap != null && userLatLng != null) {
                                if (changedListRestaurantsStateItem.size() == 1) {
                                    mapLatLng = restaurantLatLng;
                                } else {
                                    mapLatLng = userLatLng;
                                }

                                MapsFragment.this.zoomOnCurrentPosition(mMap);
                            }
                        }
                    } else {
                        extendRadiusDialog();
                    }
                });
    }

    private void zoomOnCurrentPosition(GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, INITIAL_ZOOM));
    }

    private void mMapSetUpClickListener() {
        mMap.setOnInfoWindowClickListener(marker -> Log.d(TAG, "Click on marker " + marker.getTag()));
    }

    @SuppressLint("StringFormatInvalid")
    private void extendRadiusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.alertdialog_extendradius_message, RADIUS_INIT))
                .setTitle(R.string.alertdialog_extendradius_title);
        AlertDialog dialog = builder.create();

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
            RADIUS_INIT += 1000;
            listRestaurantViewModel.initListViewMutableLiveData(userLocation);
            dialog1.dismiss();
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialog.show();
    }

    private void addMarkerResult(List<Restaurant> results) {
        mMap.clear();
        for (Restaurant result : results) {
            boolean bChosen = false;
            if (result.getNbWorkmates() != 0) {
                bChosen = true;
            }

            Marker markerRestaurant;
            if (bChosen) {
                markerRestaurant = showChosenRestaurantMarker(result);
            } else {
                markerRestaurant = showRestaurantMarker(result);
            }
            restaurantLatLng = new LatLng(result.getRestaurantLocation().getLat(), result.getRestaurantLocation().getLng());
        }
    }


    private Marker showRestaurantMarker(Restaurant result) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        result.getRestaurantLocation().getLat(),
                        result.getRestaurantLocation().getLng()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(result.getName())
                .snippet(result.getVicinity()));
    }

    private Marker showChosenRestaurantMarker(Restaurant result) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        result.getRestaurantLocation().getLat(),
                        result.getRestaurantLocation().getLng()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(result.getName())
                .snippet(result.getVicinity()));
    }


    private void enableCompassButton() {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        zoomOnCurrentPosition(mMap);
        return false;
    }

    private void moveCompassButton(View mapView) {
        try {
            assert mapView != null;
            View view = mapView.findViewWithTag("GoogleMapMyLocationButton");

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.setMargins(0, 0, 150, 40);

            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getResources().getColor(R.color.white));
        } catch (Exception ex) {
            Log.e(TAG, "MoveCompassButton() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}


