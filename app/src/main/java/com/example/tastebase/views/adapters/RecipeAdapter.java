package com.example.tastebase.views.adapters;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;
import com.example.tastebase.models.Recipe;
import com.example.tastebase.views.interfaces.CallbackRecipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;
    private CallbackRecipe listener;

    public RecipeAdapter(ArrayList<Recipe> recipes, CallbackRecipe listener) {
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

        holder.cardTitle.setText(recipe.getTitle());
        holder.cardDescription.setText(recipe.getDescription());



        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle;
        public TextView cardDescription;
        public ImageView cardImage;

        public RecipeViewHolder(View view) {
            super(view);
            cardTitle = view.findViewById(R.id.card_title);
            cardDescription = view.findViewById(R.id.card_description);
            cardDescription.setMovementMethod(new ScrollingMovementMethod());



            cardImage = view.findViewById(R.id.card_img);
        }
    }
}
