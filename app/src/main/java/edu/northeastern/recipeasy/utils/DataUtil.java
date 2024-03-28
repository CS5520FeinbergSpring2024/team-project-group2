package edu.northeastern.recipeasy.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
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
}
