package edu.northeastern.recipeasy.DiscoverRecipeRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
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
import edu.northeastern.recipeasy.activities.DiscoverRecipeItem;


public class DiscoverRecipeViewAdapter extends RecyclerView.Adapter<DiscoverRecipeViewHolder>{

    private ArrayList<DiscoverRecipeItem> recipeItemList;

    private Context context;

    public DiscoverRecipeViewAdapter(ArrayList<DiscoverRecipeItem> recipeItemList, Context context) {
        this.recipeItemList = recipeItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiscoverRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_recipe_item, parent, false);
        return new DiscoverRecipeViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DiscoverRecipeViewHolder holder, int position) {
        DiscoverRecipeItem recipeItem = recipeItemList.get(position);

        holder.recipeName.setText(recipeItem.getDishName());
        holder.calories.setText(recipeItem.getCalories() +" Calories");

        holder.description.setText(Html.fromHtml(recipeItem.getDescription(), Html.FROM_HTML_MODE_COMPACT));

        holder.image.setImageResource(R.drawable.no_image);

        new Thread(() -> {
            try {
                Bitmap picBitMap = downloadFoodPic(recipeItem.getImageUrl());

                if (picBitMap != null) {
                    holder.image.post(() -> holder.image.setImageBitmap(picBitMap));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        holder.viewRecipe.setOnClickListener(v -> {
            Uri urlLink = Uri.parse(recipeItem.getRecipeUrl());
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, urlLink);
            context.startActivity(urlIntent);
        });

    }

    @Override
    public int getItemCount() {
        return recipeItemList.size();
    }

    public Bitmap downloadFoodPic(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();

        InputStream input = conn.getInputStream();
        return BitmapFactory.decodeStream(input);

    }
}
