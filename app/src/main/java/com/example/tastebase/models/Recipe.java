package com.example.tastebase.models;

public class Recipe {
    private String name;
    private String description;
    private String ingredients;
    private String preparationSteps;
    private int cookingTime;
    private int calories;
    private String category;
    private String level;
    private String dishImgSrc;
    private String recipeImgSrc;

    public Recipe() {
        // Default constructor
    }

    public String getName() {
        return name;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Recipe setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public String getPreparationSteps() {
        return preparationSteps;
    }

    public Recipe setPreparationSteps(String preparationSteps) {
        this.preparationSteps = preparationSteps;
        return this;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public Recipe setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public int getCalories() {
        return calories;
    }

    public Recipe setCalories(int calories) {
        this.calories = calories;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Recipe setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public Recipe setLevel(String level) {
        this.level = level;
        return this;
    }

    public String getDishImgSrc() {
        return dishImgSrc;
    }

    public Recipe setDishImgSrc(String dishImgSrc) {
        this.dishImgSrc = dishImgSrc;
        return this;
    }

    public String getRecipeImgSrc() {
        return recipeImgSrc;
    }

    public Recipe setRecipeImgSrc(String recipeImgSrc) {
        this.recipeImgSrc = recipeImgSrc;
        return this;
    }
}
