package com.example.go4luncch.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.go4luncch.models.User;
import com.example.go4luncch.utils.UserHelper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    UserHelper helperFirestoreUser = new UserHelper();
    private static ArrayList<User> allWorkmates = new ArrayList<>();
    private static ArrayList<User> currentUser = new ArrayList<>();

    private static MutableLiveData<List<User>> listWorkmates;
    private static MutableLiveData<ArrayList<User>> dCurrentUser;

    private static final String COLLECTION_NAME = "users";


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference mUsersRef = db.collection(COLLECTION_NAME);

    public Task<User> getUserFirestore(String uid) {
        // Get the user from Firestore and cast it to a User model Object
        return helperFirestoreUser.getUser(uid).continueWith(task -> task.getResult().toObject(User.class));
    }


    public void updateUserFirestore(User mUser) {
        // Get the user from Firestore and cast it to a User model Object
        helperFirestoreUser.updateRestaurantChoice(mUser);
    }


    public MutableLiveData<ArrayList<User>> getFirestoreUser() {
        UserSingletonRepository userSingletonRepository = UserSingletonRepository.getInstance();
        User mUser = userSingletonRepository.getUser();
        dCurrentUser = new MutableLiveData<>();
        mUsersRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (queryDocumentSnapshots == null) {
            } else {
                //        List<DocumentSnapshot> documents = new ArrayList<>();
                currentUser.clear();
                for (DocumentSnapshot user : queryDocumentSnapshots.getDocuments()) {
                    User userObject = user.toObject(User.class);
                    if (userObject.getUid().equals(mUser.getUid())) {
                        currentUser.add(user.toObject(User.class));
                    }
                }
            }
        });
        dCurrentUser.setValue(currentUser);
        return dCurrentUser;

    }

    public MutableLiveData<List<User>> getAllUsers() {
        // Get all the user from Firestore and cast it in ArrayList format
        listWorkmates = new MutableLiveData<>();
        mUsersRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (queryDocumentSnapshots == null) {
            } else {
                List<DocumentSnapshot> documents = new ArrayList<>();
                allWorkmates.clear();
                for (DocumentSnapshot user : queryDocumentSnapshots.getDocuments()) {
                    allWorkmates.add(user.toObject(User.class));
                }
            }
        });
        listWorkmates.setValue(allWorkmates);
        return listWorkmates;
    }



    public MutableLiveData<List<User>> getAllUsersForARestaurant(String placeId) {
        // Get all the user from Firestore and cast it in ArrayList format
        listWorkmates = new MutableLiveData<>();
        mUsersRef.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (queryDocumentSnapshots == null) {
            } else {
                allWorkmates.clear();
                for (DocumentSnapshot user : queryDocumentSnapshots.getDocuments()) {
                    allWorkmates.add(user.toObject(User.class));
                }
            }
        });
        ArrayList<User> lWorkmates;
        lWorkmates = ListForARestaurant(allWorkmates, placeId);
        listWorkmates.setValue(lWorkmates);
        return listWorkmates;
    }

    private ArrayList<User> ListForARestaurant(ArrayList<User> allWorkmates, String placeId) {
        ArrayList<User> listWorkmates = new ArrayList<>();
        if (allWorkmates != null && allWorkmates.size() != 0) {
            for (User user : allWorkmates) {
                if (Objects.equals(user.getChosenRestaurantId(), placeId)) {
                    listWorkmates.add(user);
                }
            }
        }
        return listWorkmates;
    }




    public User createUserInFirestore() {
        return helperFirestoreUser.createUser();
    }

}

