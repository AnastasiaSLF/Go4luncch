package com.example.go4luncch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.go4luncch.utils.UserHelper;
import com.example.go4luncch.R;
import com.example.go4luncch.models.RestaurantChoice;
import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.SharedPreferencesRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Notifications extends Worker {

        String message = "";



    public Notifications(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
        @Override
        public Result doWork () {
            Context applicationContext = getApplicationContext();
            FillMessageNotification(applicationContext);
            return Result.success();
        }

        private Void FillMessageNotification (Context applicationContext){
            SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepository();
            RestaurantChoice restaurantChoice = sharedPreferencesRepository.getRestaurantChoice(applicationContext);
            if (restaurantChoice.getChosenRestaurantId() == null) {
                message = applicationContext.getString(R.string.notification_no_choice);
                SendNotification(message, applicationContext);
            } else if (CheckingConnection.isConnected(applicationContext)) {
                UserHelper helperFirestoreUser = new UserHelper();
                final Task<QuerySnapshot> querySnapshotTask = helperFirestoreUser.getAllUsers()
                        .addOnCompleteListener(task -> {
                            System.out.println("je passe dans onComplete");

                            if (task.isSuccessful()) {
                                ArrayList<User> users = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    for (DocumentSnapshot workmate : task.getResult()) {
                                        users.add(workmate.toObject(User.class));
                                    }
                                }
                                message = setUpMessageWithUsersData(users, restaurantChoice, applicationContext);
                                SendNotification(message, applicationContext);
                            } else {
                                System.out.println("Error getting documents: " + task.getException());
                            }
                        })
                        .addOnFailureListener(e ->
                        {
                            message = applicationContext.getString(R.string.notifications_firebase_failure);
                            SendNotification(message, applicationContext);
                        });
            } else {
                message = applicationContext.getString(R.string.notifications_no_internet_connection);
                SendNotification(message, applicationContext);
            }
            System.out.println("message mis en forme" + message);

            return null;
        }

        private void SendNotification (String message, Context applicationContext){
            System.out.println("j'envoie la notification");

            String title = "Go4Lunch";
            NotificationManager mNotificationManager =
                    (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("1",
                        "android",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("WorkManger");
                mNotificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(applicationContext, "1")
                    .setSmallIcon(R.drawable.go4lunch)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[0]);

            // Show the notification
            NotificationManagerCompat.from(applicationContext).notify(1, mBuilder.build());
        }

        public static String setUpMessageWithUsersData (ArrayList < User > users, RestaurantChoice
        restaurantChoice, Context appContext){
            ArrayList<User> usersWithoutCurrentUser = new ArrayList<>();
            String chosenRestaurantName = restaurantChoice.getChosenRestaurantName();
            String userId = restaurantChoice.getUserId();
            String chosenRestaurantAddress = restaurantChoice.getChosenRestaurantAddress();
            String chosenRestaurantId = restaurantChoice.getChosenRestaurantId();
            String wmessage;
            for (User user : users) {
                if (!user.getUid().equals(userId)) {
                    usersWithoutCurrentUser.add(user);
                }
            }
            ArrayList<String> lunchWorkmates = getWorkmates(usersWithoutCurrentUser, chosenRestaurantId);
            if (lunchWorkmates.size() > 0) {
                if (lunchWorkmates.size() == 1) {
                    wmessage =
                            appContext.getString(
                                    R.string.notification_lunch_with_one_workmate,
                                    chosenRestaurantName,
                                    chosenRestaurantAddress,
                                    lunchWorkmates.get(0));
                    return wmessage;

                } else {
                    StringBuilder workmatesString = new StringBuilder();
                    for (String workmate : lunchWorkmates) {
                        if (workmate.equals(lunchWorkmates.get(lunchWorkmates.size() - 1))) {
                            workmatesString.append(workmate);
                        } else if (workmate.equals(lunchWorkmates.get(lunchWorkmates.size() - 2))) {
                            workmatesString.append(workmate);
                            workmatesString.append(appContext.getString(R.string.notification_workmatesstring_builder_and));
                        } else {
                            workmatesString.append(workmate);
                            workmatesString.append(", ");
                        }
                    }
                    return appContext.getString(
                            R.string.notification_lunch_with_few_workmate,
                            chosenRestaurantName,
                            chosenRestaurantAddress,
                            lunchWorkmates.size(),
                            workmatesString.toString());
                }
            } else {
                return appContext.getString(R.string.notification_lunch_alone, chosenRestaurantName, chosenRestaurantAddress);
            }
        }

        private static ArrayList<String> getWorkmates (ArrayList < User > users, String restaurantId)
        {
            ArrayList<String> lunchWorkmates = new ArrayList<>();
            for (User user : users) {
                if ((user.getChosenRestaurantId() != null) && (user.getChosenRestaurantId().equals(restaurantId))) {
                    lunchWorkmates.add(user.getUserName());
                }
            }
            return lunchWorkmates;
        }


    }
