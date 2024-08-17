package com.example.tastebase.views.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;
import com.example.tastebase.controllers.RecipesController;
import com.example.tastebase.models.Recipe;
import com.example.tastebase.utilities.adapters.RecipeAdapter;
import com.example.tastebase.utilities.interfaces.CallbackRecipe;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class RecipesActivity extends AppCompatActivity implements CallbackRecipe {
    private RecipesController recipesController;
    private RecyclerView listRecipes;
    private TextInputEditText recipeSearch;
    private ImageView emptyRecipesImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_activity);

        recipesController = new RecipesController(this);

        findViews();
        recipesController.initUser();
        recipesController.setupRecyclerView(listRecipes, emptyRecipesImg, recipeSearch);
        recipesController.setupSearchFilter(recipeSearch);
    }

    private void findViews() {
        listRecipes = findViewById(R.id.listRecipes);
        recipeSearch = findViewById(R.id.recipe_search);
        emptyRecipesImg = findViewById(R.id.empty_recipes_img);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        recipesController.onRecipeClick(recipe);
    }

    @Override
    public void onRecipeDelete(int position) {
        recipesController.onRecipeDelete(position);
    }
}
