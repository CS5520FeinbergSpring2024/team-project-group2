package edu.northeastern.recipeasy.RecipeRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Recipe;

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

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeItemList.get(position);
        holder.recipeName.setText(recipe.getDishName());
        holder.cuisine.setText(recipe.getCuisine());
        holder.totalTime.setText(recipe.getCookTime());
        holder.servings.setText(recipe.getServings());
        holder.calories.setText(recipe.getCalories());

    }

    @Override
    public int getItemCount() {
        return recipeItemList.size();
    }
}
