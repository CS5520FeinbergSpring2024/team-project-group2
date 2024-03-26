package edu.northeastern.recipeasy.RecipeRecyclerView;

import android.view.View;
import android.widget.TextView;
import edu.northeastern.recipeasy.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder{
    public TextView recipeName;
    public TextView cuisine;
    public TextView totalTime;
    public TextView servings;
    public TextView dietaryRestrictions;
    public TextView calories;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipeNameID);
        cuisine = itemView.findViewById(R.id.cuisineID);
        totalTime = itemView.findViewById(R.id.totalTimeID);
        servings = itemView.findViewById(R.id.servingsID);
        dietaryRestrictions = itemView.findViewById(R.id.dietaryRestrictionsID);
        calories = itemView.findViewById(R.id.caloriesID);
    }
}
