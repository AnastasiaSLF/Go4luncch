package com.example.go4luncch.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4luncch.models.RestaurantLiked;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LikedRestaurantHelper implements OnSuccessListener<DocumentSnapshot> {

    private static final String COLLECTION_NAME = "likedRestaurant";

    public static CollectionReference getLikedRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static RestaurantLiked createLikedRestaurant(String placeId, String uid) {
        RestaurantLiked restaurantLiked = new RestaurantLiked(placeId, uid);
        getLikedRestaurantsCollection().document().set(restaurantLiked).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("TAG : Success ajout ", "Success ajout");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override

            public void onFailure(@NonNull Exception e) {
                Log.e("TAG : error ajout ", "error ajout");
            }
        });
        return restaurantLiked;
    }

    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
    }

    public void deleteLikedRestaurant(String docId) {
        if (docId != null) {
            getLikedRestaurantsCollection().document(docId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "suppression OK");
                    } else {
                        Log.d(TAG, "suppression KO");

                    }
                }
            });
        }
    }

    public Task<QuerySnapshot> queryGetDocumentId(String placeId, String userSingletonId) {
        return getLikedRestaurantsCollection().whereEqualTo("placeId", placeId)
                .whereEqualTo("uid", userSingletonId)
                .get();
    }
}

