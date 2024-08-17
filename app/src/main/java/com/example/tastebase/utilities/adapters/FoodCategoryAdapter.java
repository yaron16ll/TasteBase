package com.example.tastebase.utilities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastebase.R;
import com.example.tastebase.models.FoodCategory;
import com.example.tastebase.utilities.interfaces.CallbackCategory;

import java.util.ArrayList;
import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder> {
     private List<FoodCategory> foodCategories;

    private CallbackCategory listener;


    public FoodCategoryAdapter(List<FoodCategory> foodCategories, CallbackCategory listener) {
        this.listener = listener;
        this.foodCategories = foodCategories != null ? foodCategories : new ArrayList<>();
    }

    @NonNull
    @Override
    public FoodCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_category_layout, parent, false);
        return new FoodCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategoryViewHolder holder, int position) {
        FoodCategory foodCategory = foodCategories.get(position);
        holder.image.setImageResource(foodCategory.getImageSrc());
        holder.title.setText(foodCategory.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategotyClick(foodCategory);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodCategories.size();
    }

    public void updateCategories(List<FoodCategory> newCategories) {
        if (newCategories != null) {
            this.foodCategories = newCategories;
            notifyDataSetChanged();
        }
    }

    public static class FoodCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public FoodCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.title);
        }
    }
}
