package com.example.tastebase.views.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tastebase.R;
import com.example.tastebase.controllers.DishController;
import com.example.tastebase.models.Recipe;

public class DishActivity extends AppCompatActivity {

    private TextView ingredients;
    private TextView instructions;
    private TextView recipeCal, recipeLevel, recipeTime, recipeTitle;
    private ImageView dishImage;

    private DishController dishController;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dish_activity);

        dishController = new DishController();

        recipe = dishController.getRecipeFromIntent(getIntent());

        findViews();
        initView();
    }

    private void initView() {
        recipeCal.setText(String.valueOf(recipe.getCalories()));
        recipeTime.setText(String.valueOf(recipe.getCookingTime()));
        recipeLevel.setText(recipe.getLevel());
        recipeTitle.setText(dishController.formatRecipeName(recipe.getName()));

        instructions.setText(recipe.getPreparationSteps());
        ingredients.setText(recipe.getIngredients());

        // Load image using Glide
        Glide.with(this).load(recipe.getDishImgSrc()).into(dishImage);
    }

    private void findViews() {
        recipeTitle = findViewById(R.id.recipe_title);
        recipeCal = findViewById(R.id.recipe_cal);
        recipeLevel = findViewById(R.id.recipe_level);
        recipeTime = findViewById(R.id.recipe_time);
        instructions = findViewById(R.id.instruction);
        ingredients = findViewById(R.id.ingredients);
        dishImage = findViewById(R.id.dish_img);
    }
}
