package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private ImageView addRecipeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        findViews();
        initViews();

    }


    private void initViews() {
        addRecipeBtn.setOnClickListener(v -> {

            Intent intent = new Intent(this, CreateRecipe.class);
            startActivity(intent);


        });
    }

    private void findViews() {
         addRecipeBtn = findViewById(R.id.add_btn);
    }

}