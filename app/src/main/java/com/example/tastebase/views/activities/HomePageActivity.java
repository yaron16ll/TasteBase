package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;
import com.example.tastebase.controllers.HomePageController;
import com.example.tastebase.models.FoodCategory;
import com.example.tastebase.utilities.adapters.FoodCategoryAdapter;
import com.example.tastebase.utilities.interfaces.CallbackCategory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class HomePageActivity extends AppCompatActivity implements CallbackCategory {

    private ImageView addRecipeBtn, backLoginBtn;
    private RecyclerView recyclerView;
    private FoodCategoryAdapter foodCategoryAdapter;
    private TextInputEditText categorySearch;

    private HomePageController homePageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);

        homePageController = new HomePageController(this);

        findViews();
        initViews();
        initFoodCategories();
        initRecyclerView();
        setSearchFilter();
    }

    private void initRecyclerView() {
        // Set up RecyclerView
        foodCategoryAdapter = new FoodCategoryAdapter(homePageController.foodCategories, this);
        homePageController.setupRecyclerView(foodCategoryAdapter, recyclerView);
    }

    private void setSearchFilter() {
        // Set up search filter
        categorySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                homePageController.filterCategoriesByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });
    }

    private void initFoodCategories() {
        homePageController.initFoodCategories();
    }

    private void initViews() {
        addRecipeBtn.setOnClickListener(v -> {
            // Pass user to CreateRecipe activity
            Intent createRecipeIntent = new Intent(HomePageActivity.this, CreateRecipeActivity.class);
            startActivity(createRecipeIntent);
        });

        backLoginBtn.setOnClickListener((v -> {
            // Pass user to Login activity
            Intent backLoginBtn = new Intent(HomePageActivity.this, LoginActivity.class);
            startActivity(backLoginBtn);
            finish();
        }));
    }

    private void findViews() {
        addRecipeBtn = findViewById(R.id.create_recipe_btn);
        recyclerView = findViewById(R.id.food_categories);
        categorySearch = findViewById(R.id.category_search);
        backLoginBtn = findViewById(R.id.back_login_btn);
    }

    @Override
    public void onCategotyClick(FoodCategory foodCategory) {
        homePageController.onCategoryClick(foodCategory);

    }
}
