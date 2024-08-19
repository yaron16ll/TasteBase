package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;
import com.example.tastebase.models.Recipe;
import com.example.tastebase.models.SharedPreferencesManager;
import com.example.tastebase.models.User;
import com.example.tastebase.utilities.adapters.RecipeAdapter;
import com.example.tastebase.utilities.interfaces.CallbackRecipe;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity implements CallbackRecipe {
    private User user;
    private RecyclerView listRecipes;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes = new ArrayList<>();
    private TextInputEditText recipeSearch;
    private ImageView emptyRecipesImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_activity);

        findViews();
        initUser();
        setupRecyclerView();
        setupSearchFilter();
    }

    private void initUser() {
        String userString = SharedPreferencesManager.getInstance().getString("user", null);
        if (userString != null) {
            user = new Gson().fromJson(userString, User.class);
            if (user != null && user.getRecipes() != null) {
                recipes.addAll(getFilteredRecipes());
            }
        }
    }

    private List<Recipe> getFilteredRecipes() {
        Intent intent = getIntent();
        String stringRecipes = intent.getStringExtra("recipes");
        Type recipeListType = new TypeToken<List<Recipe>>() {
        }.getType();
        recipes = new Gson().fromJson(stringRecipes, recipeListType);
        return recipes;
    }

    private void setupRecyclerView() {
        if (!recipes.isEmpty()) {
            listRecipes.setVisibility(View.VISIBLE);
            emptyRecipesImg.setVisibility(View.GONE);
            recipeSearch.setVisibility(View.VISIBLE);

            recipeAdapter = new RecipeAdapter(recipes, this);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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

    private void setupSearchFilter() {
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

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, DishActivity.class);
        String recipeString = new Gson().toJson(recipe);
        intent.putExtra("recipe", recipeString);
        startActivity(intent);
    }

    @Override
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
                        updateDisplayedRecipes(recipes); // Update the adapter with the new list
                        updateUserInSharedPreferences(); // Update shared preferences
                    }).addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(RecipesActivity.this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    private void updateUserInSharedPreferences() {
        // Update the user object with the modified recipe list
        user.setRecipes((ArrayList<Recipe>) recipes);

        // Save the updated user object to SharedPreferences
        String updatedUserString = new Gson().toJson(user);
        SharedPreferencesManager.getInstance().putString("user", updatedUserString);
    }

    private void findViews() {
        listRecipes = findViewById(R.id.listRecipes);
        recipeSearch = findViewById(R.id.recipe_search); // Make sure to initialize this
        emptyRecipesImg = findViewById(R.id.empty_recipes_img);
    }
}
