package edu.northeastern.recipeasy.RecipeRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.activities.FullRecipeActivity;
import edu.northeastern.recipeasy.utils.DataUtil;

public class RecipeViewAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private ArrayList<Recipe> recipeItemList;
    private Context context;

    public RecipeViewAdapter(ArrayList<Recipe> recipeItemList, Context context) {
        this.recipeItemList = recipeItemList;
        this.context = context;
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeItemList.get(position);
        holder.recipeName.setText(recipe.getDishName());
        holder.cuisine.setText("Cuisine: " + recipe.getCuisine());
        holder.totalTime.setText("Time: " + recipe.getCookTime().toString() +" mins");
        holder.servings.setText("Servings: " + recipe.getServings().toString());
        holder.calories.setText("Calories: " + recipe.getCalories().toString());

        holder.vegIcon.setVisibility(View.VISIBLE);
        holder.veganIcon.setVisibility(View.VISIBLE);
        holder.gfIcon.setVisibility(View.VISIBLE);

        if (!recipe.isGlutenFree()){
            holder.gfIcon.setVisibility(View.GONE);
        }if (!recipe.isVeg()){
            holder.vegIcon.setVisibility(View.GONE);
        }if (!recipe.isVegan()){
            holder.veganIcon.setVisibility(View.GONE);
        }

        holder.seeMore.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FullRecipeActivity.class);
            intent.putExtra("recipe", recipe);
            view.getContext().startActivity(intent);
        });

        holder.image.setTag(position);

        if (!recipe.getPhotoPath().isEmpty()) {
            new Thread(() -> {
                try {
                    Bitmap picBitMap = DataUtil.downloadFoodPic(recipe.getPhotoPath());

                    holder.image.post(() -> {
                        if (holder.image.getTag() != null && holder.image.getTag().equals(position)) {
                            holder.image.setImageBitmap(picBitMap);
                        }
                    });
                } catch (Exception e) {
                }
            }).start();
        } else {
            holder.image.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public int getItemCount() {
        return recipeItemList.size();
    }

}
