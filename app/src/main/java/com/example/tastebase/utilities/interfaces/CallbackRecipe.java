package com.example.tastebase.utilities.interfaces;

import com.example.tastebase.models.Recipe;

public interface CallbackRecipe {

    void onRecipeClick(Recipe recipe);

    void onRecipeDelete(int position);

}
