package com.example.tastebase.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.tastebase.models.Recipe;

import java.io.Serializable;
import java.util.ArrayList;

public class User extends BaseObservable implements Serializable {
    private String username;
    private String password;
     private String country;
    private ArrayList<Recipe> recipes;

    public User() {
        // Default constructor
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }


    public String getCountry() {
        return country;
    }

    public User setCountry(String country) {
        this.country = country;
        return this;
    }

    public ArrayList<Recipe> getRecipes() {
        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        return recipes;
    }

    public User setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }

    public void addRecipe(Recipe recipe) {
        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        recipes.add(recipe);

    }
}
