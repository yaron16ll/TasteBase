package com.example.tastebase.models;

public class Recipe {
    private String title;
    private String description;
    private String imageSrc;


    public Recipe( ) {
     }


    public Recipe setTitle(String title) {
        this.title = title;
        return this;
    }

    public Recipe setDescription(String description) {
        this.description = description;
        return this;
    }

    public Recipe setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageSrc() {
        return imageSrc;
    }
}
