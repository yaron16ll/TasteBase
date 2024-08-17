package com.example.tastebase.controllers;

import android.content.Intent;

import com.example.tastebase.models.Recipe;
import com.google.gson.Gson;

public class DishController {

    public Recipe getRecipeFromIntent(Intent intent) {
        String recipeString = intent.getStringExtra("recipe");
        return new Gson().fromJson(recipeString, Recipe.class);
    }

    public String formatRecipeName(String recipeName) {
        return String.join("\n", recipeName.split("\\.\\s+"));
    }
}
