package com.example.tastebase.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tastebase.R;
import com.example.tastebase.models.Recipe;
import com.example.tastebase.models.SharedPreferencesManager;
import com.example.tastebase.models.User;
import com.example.tastebase.controllers.CreateRecipeController;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CreateRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int IMAGE_PICK_REQUEST_CODE = 1;
    private static final int DEFAULT_IMAGE_RES_ID = R.drawable.upload_image;

    private EditText editTextRecipeName, editTextRecipeDescription, editTextIngredients, editTextPreparationSteps, editTextCookingTime, editTextRecipeCalories;
    private Spinner spinnerCategory, spinnerLevel;
    private ImageView dishImgSrc, recipeImgSrc;
    private Button buttonSubmitRecipe;
    private ProgressBar progressBar;

    private boolean isDishImgPickerActive = false;
    private User currentUser;
    private Uri dishImageUri, recipeImageUri;

    private CreateRecipeController recipeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe_activity);

        getUser();
        recipeController = new CreateRecipeController(currentUser);
        findViews();
        initViews();
        setUpTextWatchers();
        checkFields();
    }

    private void setUpTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        editTextRecipeName.addTextChangedListener(textWatcher);
        editTextRecipeDescription.addTextChangedListener(textWatcher);
        editTextIngredients.addTextChangedListener(textWatcher);
        editTextPreparationSteps.addTextChangedListener(textWatcher);
        editTextCookingTime.addTextChangedListener(textWatcher);
        editTextRecipeCalories.addTextChangedListener(textWatcher);
    }

    private void checkFields() {
        boolean allFieldsFilled = !editTextRecipeName.getText().toString().isEmpty()
                && !editTextRecipeDescription.getText().toString().isEmpty()
                && !editTextIngredients.getText().toString().isEmpty()
                && !editTextPreparationSteps.getText().toString().isEmpty()
                && !editTextCookingTime.getText().toString().isEmpty()
                && !editTextRecipeCalories.getText().toString().isEmpty()
                && dishImageUri != null
                && recipeImageUri != null
                && spinnerCategory.getSelectedItemPosition() != 0
                && spinnerLevel.getSelectedItemPosition() != 0;

        buttonSubmitRecipe.setEnabled(allFieldsFilled);
    }

    private void getUser() {
        String userString = SharedPreferencesManager.getInstance().getString("user", null);
        currentUser = new Gson().fromJson(userString, User.class);

        if (currentUser.getRecipes() == null) {
            currentUser.setRecipes(new ArrayList<>());
        }
    }

    private void findViews() {
        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextRecipeDescription = findViewById(R.id.recipe_descriptoin); // Fixed typo
        editTextIngredients = findViewById(R.id.editTextIngredients);
        editTextPreparationSteps = findViewById(R.id.editTextPreparationSteps);
        editTextCookingTime = findViewById(R.id.editTextCookingTime);
        editTextRecipeCalories = findViewById(R.id.editTextRecipeCalories);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerLevel = findViewById(R.id.spinner_level);
        dishImgSrc = findViewById(R.id.dish_image);
        recipeImgSrc = findViewById(R.id.recipe_image);
        buttonSubmitRecipe = findViewById(R.id.buttonSubmitRecipe);
        progressBar = findViewById(R.id.progressBar);

        // Set default image placeholders
        dishImgSrc.setImageResource(DEFAULT_IMAGE_RES_ID);
        recipeImgSrc.setImageResource(DEFAULT_IMAGE_RES_ID);
    }

    private void initViews() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_level, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(levelAdapter);

        spinnerCategory.setOnItemSelectedListener(this);
        spinnerLevel.setOnItemSelectedListener(this);

        findViewById(R.id.dish_img_btn).setOnClickListener(v -> {
            isDishImgPickerActive = true;
            openImagePicker();
        });

        findViewById(R.id.recipe_img_btn).setOnClickListener(v -> {
            isDishImgPickerActive = false;
            openImagePicker();
        });

        buttonSubmitRecipe.setOnClickListener(v -> submit());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                if (isDishImgPickerActive) {
                    dishImageUri = imageUri;
                    Glide.with(this).load(dishImageUri).apply(new RequestOptions().override(800, 800)).into(dishImgSrc);
                } else {
                    recipeImageUri = imageUri;
                    Glide.with(this).load(recipeImageUri).apply(new RequestOptions().override(800, 800)).into(recipeImgSrc);
                }
                checkFields();
            }
        }
    }

    private void submit() {
        showLoading(true);

        if (dishImageUri != null && recipeImageUri != null) {
            recipeController.uploadImageToStorage(dishImageUri, dishImageUrl -> {
                recipeController.uploadImageToStorage(recipeImageUri, recipeImageUrl -> {
                    Recipe newRecipe = createRecipeObject(dishImageUrl, recipeImageUrl);
                    recipeController.submitRecipe(newRecipe, task -> {
                        showLoading(false);
                        if (task.isSuccessful()) {
                            showToast("The recipe is successfully submitted");
                        } else {
                            showToast("Failed to add recipe.");
                        }
                    });
                }, e -> {
                    showToast("Failed to upload recipe image.");
                    showLoading(false);
                });
            }, e -> {
                showToast("Failed to upload dish image.");
                showLoading(false);
            });
        } else {
            showToast("Please pick images for both dish and recipe.");
            showLoading(false);
        }
    }

    private Recipe createRecipeObject(String dishImageUrl, String recipeImageUrl) {
        return new Recipe()
                .setName(editTextRecipeName.getText().toString())
                .setDescription(editTextRecipeDescription.getText().toString())
                .setIngredients(editTextIngredients.getText().toString())
                .setPreparationSteps(editTextPreparationSteps.getText().toString())
                .setCookingTime(Integer.parseInt(editTextCookingTime.getText().toString()))
                .setCalories(Integer.parseInt(editTextRecipeCalories.getText().toString()))
                .setCategory(spinnerCategory.getSelectedItem().toString())
                .setLevel(spinnerLevel.getSelectedItem().toString())
                .setDishImgSrc(dishImageUrl)
                .setRecipeImgSrc(recipeImageUrl);
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        buttonSubmitRecipe.setEnabled(!isLoading);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkFields();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
