package edu.northeastern.recipeasy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.recipeasy.domain.Recipe;

public class DataUtil {

    public static ArrayList<String> parseStringArray(DataSnapshot snapshot) {
        ArrayList<String> items = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            String item = childSnapshot.getValue(String.class);
            items.add(item);
        }
        return items;
    }

    public static ArrayList<Recipe> parseRecipes(DataSnapshot snapshot){
        ArrayList<Recipe> recipes = new ArrayList<>();
        List<DataSnapshot> snapshotList = new ArrayList<>();

        for(DataSnapshot recipeSnapshot : snapshot.getChildren()) {
            snapshotList.add(recipeSnapshot);
        }
        for (int i = snapshotList.size() -1; i>=0; i--) {
            DataSnapshot recipeSnapshot = snapshotList.get(i);
            String author = recipeSnapshot.child("authorName").getValue(String.class);
            String dishName = recipeSnapshot.child("dishName").getValue(String.class);
            Integer calories = recipeSnapshot.child("calories").getValue(Integer.class);
            Integer cookTime = recipeSnapshot.child("cookTime").getValue(Integer.class);
            Integer prepTime = recipeSnapshot.child("prepTime").getValue(Integer.class);
            Integer servings = recipeSnapshot.child("servings").getValue(Integer.class);
            Integer views = recipeSnapshot.child("views").getValue(Integer.class);
            boolean veg = recipeSnapshot.child("veg").getValue(Boolean.class);
            boolean vegan = recipeSnapshot.child("vegan").getValue(Boolean.class);
            boolean glutenFree = recipeSnapshot.child("glutenFree").getValue(Boolean.class);
            String cuisine = recipeSnapshot.child("cuisine").getValue(String.class);
            String ingredients = recipeSnapshot.child("ingredients").getValue(String.class);
            String steps = recipeSnapshot.child("steps").getValue(String.class);
            String photoPath = recipeSnapshot.child("photoPath").getValue(String.class);

            recipes.add(new Recipe(author, dishName, cuisine, prepTime, cookTime, servings, veg, vegan, glutenFree,
                    ingredients, steps, photoPath, calories, views ));
        }
        return recipes;
    }
    public static void fetchAllRecipes(ValueEventListener listener) {
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference().child("recipes");
        recipesRef.addListenerForSingleValueEvent(listener);
    }

    public static Bitmap downloadFoodPic(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();

        InputStream input = conn.getInputStream();
        return BitmapFactory.decodeStream(input);

    }
}
