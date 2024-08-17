package com.example.tastebase.controllers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.tastebase.models.SharedPreferencesManager;
import com.example.tastebase.models.User;
import com.example.tastebase.utilities.interfaces.GetUsersFromDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LoginController {
    private DatabaseReference usersRef;
    private Context context;

    public LoginController(Context context) {
        this.context = context;
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void initSharedPreferences() {
        SharedPreferencesManager.init(context);
    }

    public void validateUser(String username, String password, GetUsersFromDB callback) {
        getUsersFromDB(new GetUsersFromDB() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                User user = isUserRegistered(users, username, password);
                callback.onSuccess(user != null ? users : null);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    public User isUserRegistered(ArrayList<User> users, String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void getUsersFromDB(final GetUsersFromDB callback) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    public void saveUserToSharedPreferences(User user) {
        String userString = new Gson().toJson(user);
        SharedPreferencesManager.getInstance().putString("user", userString);
    }
}
