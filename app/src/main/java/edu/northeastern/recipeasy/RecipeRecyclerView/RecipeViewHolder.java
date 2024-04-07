package edu.northeastern.recipeasy.RecipeRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.northeastern.recipeasy.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder{
    public TextView recipeName;
    public TextView cuisine;
    public TextView totalTime;
    public TextView servings;
    public ImageView image;

    public TextView calories;
    public Button seeMore;
    public ImageView vegIcon;
    public ImageView veganIcon;
    public ImageView gfIcon;


    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.recipeNameID);
        cuisine = itemView.findViewById(R.id.cuisineID);
        totalTime = itemView.findViewById(R.id.totalTimeID);
        servings = itemView.findViewById(R.id.servingsID);
        vegIcon = itemView.findViewById(R.id.vegIcon);
        veganIcon = itemView.findViewById(R.id.veganIcon);
        gfIcon = itemView.findViewById(R.id.gfIcon);
        calories = itemView.findViewById(R.id.caloriesID);
        image = itemView.findViewById(R.id.imageViewID);
        seeMore = itemView.findViewById(R.id.viewProfileButtonID);
    }
}