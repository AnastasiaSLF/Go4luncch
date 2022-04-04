package com.example.go4luncch;

import com.example.go4luncch.models.RestaurantChoice;
import com.example.go4luncch.repositories.SharedPreferencesRepository;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.UserRepository;
import com.example.go4luncch.repositories.UserSingletonRepository;
import com.example.go4luncch.utils.CheckingConnection;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;



@RequiresApi(api = Build.VERSION_CODES.N)
public class LoginActivity extends BaseActivity{

    private static final String TAG = "Login Activity";
    public FirebaseAuth firebaseAuth;
    SwipeRefreshLayout swipeRefresh;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        swipeRefresh = findViewById(R.id.signin_no_connection_refresh);
        swipeRefresh.setOnRefreshListener(() -> {
            checkConnectivity();
            swipeRefresh.setRefreshing(false);
        });
        checkConnectivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkConnectivity() {
        if (!CheckingConnection.isConnected(getApplicationContext())) {
            swipeRefresh.setVisibility(View.VISIBLE);
        } else {
            swipeRefresh.setVisibility(View.GONE);
            if (isCurrentUserLogged()) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                saveUserSingleton(userId);
                startMainActivity();
            } else {
                setContentView(R.layout.activity_login);
                SignIn();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveUserSingleton(String userId) {
        UserRepository userRepository = new UserRepository();
        userRepository.getUserFirestore(userId).addOnSuccessListener(user -> {
            User singletonUser = new
                    User();
            singletonUser.setUid(userId);
            saveSingleton(user);
        });
    }

    private void SignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());


        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setIsSmartLockEnabled(false, true)
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            UserRepository userRepository = new UserRepository();
            User mUser = userRepository.createUserInFirestore();
            saveSingleton(mUser);
            startMainActivity();
        } else {
            Log.w(TAG,
                    "signInWithCredential:failure",
                    response.getError());
            Toast.makeText(LoginActivity.this, "Authentication Failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveSingleton(User mUser) {
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        userSingletonRepository.setUser(mUser);

        Context context = getApplicationContext();
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepository();
        RestaurantChoice restaurantChoice = new RestaurantChoice();
        restaurantChoice.setUserId(mUser.getUid());
        restaurantChoice.setChosenRestaurantId(mUser.getChosenRestaurantId());
        restaurantChoice.setChosenRestaurantName(mUser.getChosenRestaurantName());
        restaurantChoice.setChosenRestaurantAddress(mUser.getChosenRestaurantAddress());
        restaurantChoice.setChosenDate(mUser.getChosenRestaurantDate());
        sharedPreferencesRepository.saveRestaurantChoice(context, restaurantChoice);
    }
}


