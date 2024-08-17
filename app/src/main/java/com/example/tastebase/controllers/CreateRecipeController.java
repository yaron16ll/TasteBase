package com.example.tastebase.controllers;

import android.net.Uri;

import com.example.tastebase.models.Recipe;
import com.example.tastebase.models.SharedPreferencesManager;
import com.example.tastebase.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.UUID;

public class CreateRecipeController {
    private User currentUser;
    private DatabaseReference userRef;
    private SharedPreferencesManager preferencesManager;

    public CreateRecipeController(User currentUser) {
        this.currentUser = currentUser;
        this.userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUsername());
        this.preferencesManager = SharedPreferencesManager.getInstance();
    }

    public void submitRecipe(Recipe newRecipe, OnCompleteListener<Void> onCompleteListener) {
        currentUser.getRecipes().add(newRecipe);
        userRef.child("recipes").setValue(currentUser.getRecipes())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userString = new Gson().toJson(currentUser);
                        preferencesManager.putString("user", userString);
                    }
                    onCompleteListener.onComplete(task);
                });
    }

    public void uploadImageToStorage(Uri imageUri, OnSuccessListener<String> onSuccessListener, OnFailureListener onFailureListener) {
        if (imageUri == null) {
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> onSuccessListener.onSuccess(uri.toString()))
                        .addOnFailureListener(onFailureListener))
                .addOnFailureListener(onFailureListener);
    }
}


