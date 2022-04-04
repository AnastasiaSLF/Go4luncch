package com.example.go4luncch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.UserRepository;

import java.util.List;

public class WorkmatesViewModel extends ViewModel {
    UserRepository userRepository;

    private static MutableLiveData<List<User>> listWorkmates = new MutableLiveData<>();

    public WorkmatesViewModel(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<List<User>> getWorkmates() {
        listWorkmates = userRepository.getAllUsers();
        return listWorkmates;
    }

}