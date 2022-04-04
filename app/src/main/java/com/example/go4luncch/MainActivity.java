package com.example.go4luncch;

import static com.example.go4luncch.fragments.MapsFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4luncch.NearbySearch.Location;
import com.example.go4luncch.autocomplete.Prediction;
import com.example.go4luncch.fragments.MapsFragment;
import com.example.go4luncch.fragments.RestaurantsFragment;
import com.example.go4luncch.fragments.SettingFragment;
import com.example.go4luncch.fragments.WorkMatesFragment;
import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.UserSingletonRepository;
import com.example.go4luncch.utils.Details;
import com.example.go4luncch.viewmodels.RestaurantsViewModel;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MAIN_ACTIVITY";
    private static final String API_AUTOCOMPLETE_FILTER_KEYWORD = "restaurant";



    public static int RADIUS_INIT = 1000;
    public static final int RADIUS_MAX = 5000;


    String restaurantChoiceId;
    public Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TextView userMail;
    private TextView userName;
    private ImageView userPicture;
    private RestaurantsViewModel listRestaurantViewModel;
    private AutoCompleteTextView autoCompleteTextView;
    private MenuItem clearButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();
        configureInitialState();
        autoCompleteTextListener();

    }

    private void configureInitialState() {
        listRestaurantViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance()).get(RestaurantsViewModel.class);

        Fragment fragment = new MapsFragment();
        mToolbar.setTitle(R.string.title_mapview);
        showFragment(fragment);
    }


    private void configureToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextViewPlace);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        clearButton = menu.findItem(R.id.main_activity_menu_clear);
        clearButton.setVisible(false);
        return true;
    }


    private void configureNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            final int navigation_mapview_id = R.id.map_btn;
            final int navigation_listview_id = R.id.list_btn;
            final int navigation_workmates_id = R.id.workmates_btn;

            switch (item.getItemId()) {
                case navigation_mapview_id:
                    showSearch();
                    mToolbar.setTitle(R.string.bottom_navigation_menu_map);
                    fragment = new MapsFragment();
                    showFragment(fragment);
                    break;

                case navigation_listview_id:
                    showSearch();
                    mToolbar.setTitle(R.string.bottom_navigation_menu_list);
                    fragment = new RestaurantsFragment();
                    showFragment(fragment);
                    break;

                case navigation_workmates_id:
                    hideSearch();
                    mToolbar.setTitle(R.string.bottom_navigation_menu_workmates);
                    fragment = new WorkMatesFragment();
                    showFragment(fragment);
                    break;
            }
            return true;
        });
    }
    private void showSearch() {
        findViewById(R.id.main_activity_menu_search).setVisibility(View.VISIBLE);
    }

    private void hideSearch() {
        if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
            autoCompleteTextView.setVisibility(View.GONE);
        }
        findViewById(R.id.main_activity_menu_search).setVisibility(View.GONE);
    }


    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                configureDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        userMail = header.findViewById(R.id.mail_header);
        userName = header.findViewById(R.id.name_header);
        userPicture = header.findViewById(R.id.photo_header);


    }

    private void configureDrawer() {
        User mUser;
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        mUser = userSingletonRepository.getUser();

        userName.setText(mUser.getUserName());
        userMail.setText(mUser.getUserMail());
        if (mUser.getUrlPicture() != null) {
            Glide.with(getApplicationContext())
                    .load(Objects.requireNonNull(mUser.getUrlPicture()))
                    .circleCrop()
                    .into(userPicture);
        }
        restaurantChoiceId = mUser.getChosenRestaurantId();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final int main_drawer_lunch_id = R.id.menu_drawer_lunch;
        final int main_drawer_settings_id = R.id.menu_drawer_settings;
        final int main_drawer_logout_id = R.id.menu_drawer_Logout;
        switch (id) {
            case main_drawer_lunch_id:
                yourLunchClick();
                return true;
            case main_drawer_settings_id:
                settings();
                return true;
            case main_drawer_logout_id:
                deleteAuthAndLogOut();
                return true;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void yourLunchClick() {
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        if (restaurantChoiceId == null || restaurantChoiceId.equals("")) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.no_restaurant_choose, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Details.openDetailsFragment(this, restaurantChoiceId);
        }
    }

    private void settings() {
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        Fragment fragment = new SettingFragment();
        showFragment(fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteAuthAndLogOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(currentFragment instanceof MapsFragment
                || currentFragment instanceof RestaurantsFragment
                || currentFragment instanceof WorkMatesFragment)) {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showFragment(new MapsFragment());
            } else {
                Log.i(TAG, "LOCATION permission was NOT granted in MapFragment view.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == RestaurantsFragment.RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            Log.i(TAG, "Received response for LOCATION permission request from RestaurantFragment.");

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "LOCATION permission has now been granted in RestaurantsFragment view.");
                showFragment(new MapsFragment());
            } else {
                Log.i(TAG, "LOCATION permission was NOT granted.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final int main_activity_menu_search = R.id.main_activity_menu_search;
        final int main_activity_menu_clear = R.id.main_activity_menu_clear;

        switch (id) {
            case main_activity_menu_search:
                showOrHideAutocompleteItem();
                return true;
            case main_activity_menu_clear:
                autoCompleteTextView.setText("");
                hideAutocompleteItem();
                reinitializeMap();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reinitializeMap() {
        Location userLocation = (listRestaurantViewModel.getLocationForReinitMap());
        String userLocationStr = userLocation.getLat() + "," + userLocation.getLng();

        listRestaurantViewModel.initListViewMutableLiveData(userLocationStr);
    }

    private void showOrHideAutocompleteItem() {
        if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
            autoCompleteTextView.setVisibility(View.GONE);
            clearButton.setVisible(false);
        } else {
            autoCompleteTextView.setVisibility(View.VISIBLE);
            clearButton.setVisible(true);
        }
    }
    private void hideAutocompleteItem() {
        clearButton.setVisible(false);
        autoCompleteTextView.setVisibility(View.GONE);
    };
    private void autoCompleteTextListener() {
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    listRestaurantViewModel.getAutocomplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    clearButton.setVisible(true);
                    findViewById(R.id.main_activity_menu_search).setVisibility(View.GONE);
                } else {
                    clearButton.setVisible(false);
                    findViewById(R.id.main_activity_menu_search).setVisibility(View.VISIBLE);
                    reinitializeMap();
                }
            }
        });

        listRestaurantViewModel.getListRestaurantAutoComplete().observe(this, this::filterAutocompleteResults);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            MutableLiveData<List<Prediction>> predictions = listRestaurantViewModel.getListRestaurantAutoComplete();
            if (parent.getItemAtPosition(position) == getResources().getString(R.string.autoCompleteTextView_noresult)) {
                autoCompleteTextView.setText("");
            } else {
                for (Prediction prediction :
                        Objects.requireNonNull(predictions.getValue())) {
                    if (parent.getItemAtPosition(position) == prediction.getStructured_formatting().getMain_text()) {
                        String placeId = prediction.getPlace_id();

                        alimRestaurantMutableLiveData(placeId);
                    }
                }
            }
        });
    }

    private void alimRestaurantMutableLiveData(String placeId) {
        listRestaurantViewModel.callPlaces(placeId);
        listRestaurantViewModel.getDetailRestaurant().observe(this,
                changedDetailRestaurant -> {
                });
    }

    private void filterAutocompleteResults(List<Prediction> predictionAPIAutocompletes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_dropdown_item_1line);
        adapter.setNotifyOnChange(true);
        autoCompleteTextView.setAdapter(adapter);

        for (Prediction prediction : predictionAPIAutocompletes) {
            if (prediction.getTypes().contains(API_AUTOCOMPLETE_FILTER_KEYWORD)) {
                Log.d(TAG, "getAutocompleteSearch : prediction = " + prediction.getDescription());
                adapter.add(prediction.getStructured_formatting().getMain_text());
                adapter.notifyDataSetChanged();
            }
        }
        if (adapter.getCount() == 0) {
            adapter.add(getResources().getString(R.string.autoCompleteTextView_noresult));
            adapter.notifyDataSetChanged();
        }
    }
}
