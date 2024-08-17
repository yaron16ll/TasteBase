package com.example.tastebase.controllers;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;
import com.example.tastebase.models.FoodCategory;
import com.example.tastebase.models.Recipe;
import com.example.tastebase.models.SharedPreferencesManager;
import com.example.tastebase.models.User;
import com.example.tastebase.utilities.adapters.FoodCategoryAdapter;
import com.example.tastebase.views.activities.RecipesActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomePageController {

    private Context context;
    public List<FoodCategory> foodCategories;
    private FoodCategoryAdapter foodCategoryAdapter;

    public HomePageController(Context context) {
        this.context = context;
        initFoodCategories();
    }

    public void initFoodCategories() {
        foodCategories = new ArrayList<>();
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.breakfast).setTitle("Breakfast"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.lunch).setTitle("Lunch"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.dinner).setTitle("Dinner"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.dessert).setTitle("Desserts"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.salads).setTitle("Salads"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.soup).setTitle("Soups"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.vegetarian).setTitle("Vegetarian"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.vegan).setTitle("Vegan"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.seafood).setTitle("Seafood"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.poultry).setTitle("Poultry"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.meat).setTitle("Beef"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.pasta).setTitle("Pasta"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.healthy).setTitle("Healthy"));
        foodCategories.add(new FoodCategory().setImageSrc(R.drawable.glutenfree).setTitle("Gluten-Free"));
    }

    public void setupRecyclerView(FoodCategoryAdapter foodCategoryAdapter, RecyclerView recyclerView) {
        this.foodCategoryAdapter = foodCategoryAdapter;
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3)); // 3 columns
        recyclerView.setAdapter(foodCategoryAdapter);
    }

    public void filterCategoriesByName(String query) {
        if (query == null || foodCategories == null) return;

        List<FoodCategory> filteredCategories = new ArrayList<>();
        String queryLower = query.toLowerCase().trim();

        if (queryLower.isEmpty()) {
            // Show all categories if query is empty
            filteredCategories.addAll(foodCategories);
        } else {
            for (FoodCategory foodCategory : foodCategories) {
                if (foodCategory.getTitle().toLowerCase().contains(queryLower)) {
                    filteredCategories.add(foodCategory);
                }
            }
        }

        updateDisplayedCategories(filteredCategories);
    }

    private void updateDisplayedCategories(List<FoodCategory> filteredCategories) {
        if (filteredCategories == null) {
            filteredCategories = new ArrayList<>();
        }
        foodCategoryAdapter.updateCategories(filteredCategories);
    }

    public void onCategoryClick(FoodCategory foodCategory) {
        Intent intent = new Intent(context, RecipesActivity.class);
        List<Recipe> filteredRecipes = filterRecipesByCategory(foodCategory);

        String recipesString = new Gson().toJson(filteredRecipes);
        intent.putExtra("recipes", recipesString);

        context.startActivity(intent);
    }

    private List<Recipe> filterRecipesByCategory(FoodCategory foodCategory) {
        List<Recipe> filteredRecipes = new ArrayList<>();

        String userString = SharedPreferencesManager.getInstance().getString("user", null);
        if (userString != null) {
            User user = new Gson().fromJson(userString, User.class);
            if (user != null && user.getRecipes() != null) {
                for (Recipe recipe : user.getRecipes()) {
                    if (recipe != null && recipe.getCategory() != null &&
                            recipe.getCategory().equalsIgnoreCase(foodCategory.getTitle().trim())) {
                        filteredRecipes.add(recipe);
                    }
                }
            }
        }

        return filteredRecipes;
    }
}
