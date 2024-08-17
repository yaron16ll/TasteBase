package com.example.tastebase.models;

public class FoodCategory {
    private String title;
    private int imageResId;


    public FoodCategory() {
    }


    public FoodCategory setTitle(String title) {
        this.title = title;
        return this;
    }

    public FoodCategory setImageSrc(int imageResId) {
        this.imageResId = imageResId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public int getImageSrc() {
        return imageResId;
    }
}
