package edu.northeastern.recipeasy.DiscoverRecipeRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.recipeasy.R;

public class DiscoverRecipeViewHolder extends RecyclerView.ViewHolder{

    public TextView recipeName;
    public TextView calories;
    public ImageView image;
    public Button viewRecipe;

    public TextView description;

    public DiscoverRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipeNameID);
        calories = itemView.findViewById(R.id.caloriesID);
        image = itemView.findViewById(R.id.imageViewID);
        viewRecipe = itemView.findViewById(R.id.viewRecipeButtonID);
        description = itemView.findViewById(R.id.descriptionId);


    }

}
