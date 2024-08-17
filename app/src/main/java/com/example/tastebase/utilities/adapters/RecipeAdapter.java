package com.example.tastebase.utilities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tastebase.R;
import com.example.tastebase.models.Recipe;
import com.example.tastebase.utilities.interfaces.CallbackRecipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private CallbackRecipe listener;

    public RecipeAdapter(List<Recipe> recipes, CallbackRecipe listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.cardTitle.setText(recipe.getName());
        holder.cardDescription.setText(recipe.getDescription());
        holder.cardCookingTime.setText(String.format("%d min", recipe.getCookingTime()));
        holder.cardCalories.setText(String.format("%d Cal", recipe.getCalories()));
        holder.cardLevel.setText(recipe.getLevel());

        // Load image using Glide
        Glide.with(holder.itemView.getContext()).load(recipe.getRecipeImgSrc()).into(holder.cardImage);


        // Click listener for item selection
        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeDelete(position);
            }
        });

        // Click listener for item selection
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        if (newRecipes != null) {
            this.recipes = newRecipes;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle;
        public TextView cardDescription;
        public TextView cardCookingTime;
        public TextView cardCalories;
        public TextView cardLevel;
        public ImageView cardImage;
        public ImageView deleteBtn;

        public RecipeViewHolder(View view) {
            super(view);
            cardTitle = view.findViewById(R.id.card_title);
            cardDescription = view.findViewById(R.id.card_description);
            cardCookingTime = view.findViewById(R.id.card_cooking_time);
            cardCalories = view.findViewById(R.id.card_calories);
            cardLevel = view.findViewById(R.id.card_level);
            cardImage = view.findViewById(R.id.card_img);
            deleteBtn = view.findViewById(R.id.delete_btn);
        }
    }
}
