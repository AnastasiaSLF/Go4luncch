package com.example.go4luncch;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4luncch.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
        protected static User user;

        protected static FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }

        protected Boolean isCurrentUserLogged() { return (this.getCurrentUser() != null); }



    }
