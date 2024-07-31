package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;
import com.example.tastebase.models.Recipe;
import com.example.tastebase.views.adapters.RecipeAdapter;
import com.example.tastebase.views.interfaces.CallbackRecipe;

import java.util.ArrayList;

public class Recipes extends AppCompatActivity implements CallbackRecipe {

    private RecyclerView listRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        findViews();

        ArrayList<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe()
                .setTitle("Chicken Tikka Masala")
                .setDescription("Experience the rich and bold flavors of our Spicy Chicken Curry, a delectable dish featuring tender chicken pieces simmered in a thick, spicy tomato sauce. This curry is infused with aromatic spices like cumin, coriander, and turmeric, giving it a vibrant color and a tantalizing aroma. The heat from the chilies adds a fiery kick, balanced perfectly by the sweetness of caramelized onions and the tang of tomatoes. Each bite is a burst of flavors, from the deep, earthy notes of the spices to the juicy, succulent chicken. Ideal for those who love a bit of heat, this dish is best served with steamed rice or warm naan bread, making it a perfect meal for any occasion. Dive into this culinary delight and savor the perfect blend of spices and textures.")
                .setImageSrc("tikka_masala.jpg"));
        recipes.add(new Recipe()
                .setTitle("Chicken Tikka Masala")
                .setDescription("Experience the rich and bold flavors of our Spicy Chicken Curry, a delectable dish featuring tender chicken pieces simmered in a thick, spicy tomato sauce. This curry is infused with aromatic spices like cumin, coriander, and turmeric, giving it a vibrant color and a tantalizing aroma. The heat from the chilies adds a fiery kick, balanced perfectly by the sweetness of caramelized onions and the tang of tomatoes. Each bite is a burst of flavors, from the deep, earthy notes of the spices to the juicy, succulent chicken. Ideal for those who love a bit of heat, this dish is best served with steamed rice or warm naan bread, making it a perfect meal for any occasion. Dive into this culinary delight and savor the perfect blend of spices and textures.")
                .setImageSrc("tikka_masala.jpg"));
        recipes.add(new Recipe()
                .setTitle("Chicken Tikka Masala")
                .setDescription("Experience the rich and bold flavors of our Spicy Chicken Curry, a delectable dish featuring tender chicken pieces simmered in a thick, spicy tomato sauce. This curry is infused with aromatic spices like cumin, coriander, and turmeric, giving it a vibrant color and a tantalizing aroma. The heat from the chilies adds a fiery kick, balanced perfectly by the sweetness of caramelized onions and the tang of tomatoes. Each bite is a burst of flavors, from the deep, earthy notes of the spices to the juicy, succulent chicken. Ideal for those who love a bit of heat, this dish is best served with steamed rice or warm naan bread, making it a perfect meal for any occasion. Dive into this culinary delight and savor the perfect blend of spices and textures.")
                .setImageSrc("tikka_masala.jpg"));

        RecipeAdapter adapter = new RecipeAdapter(recipes, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        listRecipes.setLayoutManager(linearLayoutManager);
        listRecipes.setAdapter(adapter);
        listRecipes.setNestedScrollingEnabled(false); // Disable nested scrolling]

    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, Dish.class);
        startActivity(intent);
    }

    private void findViews() {
        listRecipes = findViewById(R.id.listRecipes);
    }
}
