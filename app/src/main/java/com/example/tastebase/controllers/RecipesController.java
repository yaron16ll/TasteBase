package com.example.tastebase.controllers;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.models.Recipe;
import com.example.tastebase.models.SharedPreferencesManager;
import com.example.tastebase.models.User;
import com.example.tastebase.utilities.adapters.RecipeAdapter;
import com.example.tastebase.views.activities.DishActivity;
import com.example.tastebase.views.activities.RecipesActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipesController {

    private User user;
    private List<Recipe> recipes;
    private Context context;
    private RecipeAdapter recipeAdapter;

    public RecipesController(Context context) {
        this.context = context;
        recipes = new ArrayList<>();
    }

    public void initUser() {
        String userString = SharedPreferencesManager.getInstance().getString("user", null);
        if (userString != null) {
            user = new Gson().fromJson(userString, User.class);
            if (user != null && user.getRecipes() != null) {
                recipes.addAll(getFilteredRecipes());
            }
        }
    }

    public List<Recipe> getFilteredRecipes() {
        Intent intent = ((RecipesActivity) context).getIntent();
        String stringRecipes = intent.getStringExtra("recipes");
        Type recipeListType = new TypeToken<List<Recipe>>() {
        }.getType();
        recipes = new Gson().fromJson(stringRecipes, recipeListType);
        return recipes;
    }

    public void setupRecyclerView(RecyclerView listRecipes, ImageView emptyRecipesImg, TextInputEditText recipeSearch) {
        if (!recipes.isEmpty()) {
            listRecipes.setVisibility(View.VISIBLE);
            emptyRecipesImg.setVisibility(View.GONE);
            recipeSearch.setVisibility(View.VISIBLE);

            recipeAdapter = new RecipeAdapter(recipes, (RecipesActivity) context);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

            listRecipes.setLayoutManager(linearLayoutManager);
            listRecipes.setAdapter(recipeAdapter);
            listRecipes.setNestedScrollingEnabled(false); // Disable nested scrolling
        } else {
            listRecipes.setVisibility(View.GONE);
            emptyRecipesImg.setVisibility(View.VISIBLE);
            recipeSearch.setVisibility(View.GONE);
        }
    }

    public void setupSearchFilter(TextInputEditText recipeSearch) {
        recipeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipesByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });
    }

    public void filterRecipesByName(String query) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        String queryLower = query.toLowerCase().trim();

        if (queryLower.isEmpty()) {
            filteredRecipes.addAll(recipes);
        } else {
            for (Recipe recipe : recipes) {
                if (recipe.getName().toLowerCase().contains(queryLower)) {
                    filteredRecipes.add(recipe);
                }
            }
        }

        updateDisplayedRecipes(filteredRecipes);
    }

    public void updateDisplayedRecipes(List<Recipe> filteredRecipes) {
        recipeAdapter.updateRecipes(filteredRecipes);
    }

    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(context, DishActivity.class);
        String recipeString = new Gson().toJson(recipe);
        intent.putExtra("recipe", recipeString);
        context.startActivity(intent);
    }

    public void onRecipeDelete(int position) {
        if (position >= 0 && position < recipes.size()) {
            deleteRecipeFromDB(position);
        }
    }

    private void deleteRecipeFromDB(int position) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUsername());

        // Load existing recipes from Firebase
        userRef.child("recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Recipe> firebaseRecipes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    firebaseRecipes.add(recipe);
                }

                // Remove the recipe at the specified position
                if (position >= 0 && position < firebaseRecipes.size()) {
                    firebaseRecipes.remove(position);

                    // Update Firebase with the modified list
                    userRef.child("recipes").setValue(firebaseRecipes).addOnSuccessListener(aVoid -> {
                        // Update local list and notify adapter after successful Firebase update
                        recipes.remove(position);
                        recipeAdapter.notifyDataSetChanged(); // Notify the adapter of data change
                    }).addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(context, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    public void deleteRecipeFromSharedPre() {
        String stringRecipes = new Gson().toJson(recipes);
        SharedPreferencesManager.getInstance().putString("user", stringRecipes);
    }
}
