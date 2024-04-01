package edu.northeastern.recipeasy.RecipeRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        holder.cuisine.setText(recipe.getCuisine());
        holder.totalTime.setText(recipe.getCookTime().toString());
        holder.servings.setText(recipe.getServings().toString());
        holder.calories.setText(recipe.getCalories().toString());
        holder.seeMore.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FullRecipeActivity.class);
            intent.putExtra("recipe", recipe);
            view.getContext().startActivity(intent);
        });
//        holder.image.setImageResource(R.drawable.no_image);
//        new Thread(() -> {
//            try {
//                Bitmap picBitMap = DataUtil.downloadFoodPic(recipe.getPhotoPath());
//
//                if(! recipe.getPhotoPath().equals("")){
//                    if (picBitMap != null) {
//                        holder.image.post(() -> holder.image.setImageBitmap(picBitMap));
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();


    }

    @Override
    public int getItemCount() {
        return recipeItemList.size();
    }

}
