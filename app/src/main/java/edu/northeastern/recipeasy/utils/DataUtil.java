package edu.northeastern.recipeasy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.domain.User;

public class DataUtil {

    public static ArrayList<String> parseStringArray(DataSnapshot snapshot) {
        ArrayList<String> items = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            String item = childSnapshot.getValue(String.class);
            items.add(item);
        }
        return items;
    }

    private static User parseIndividualUsers(DataSnapshot userSnapshot ) {
        String username = userSnapshot.child("username").getValue(String.class);
        return (new User(username));
    }

    public static ArrayList<User> parseUsers(DataSnapshot userSnapshot) {
        ArrayList<User> users = new ArrayList<>();

        for (DataSnapshot u : userSnapshot.getChildren()) {
            users.add(parseIndividualUsers(u));
        }
        return users;
    }
    public static ArrayList<Recipe> parseRecipes(DataSnapshot snapshot){
        ArrayList<Recipe> recipes = new ArrayList<>();
        List<DataSnapshot> snapshotList = new ArrayList<>();

        for(DataSnapshot recipeSnapshot : snapshot.getChildren()) {
            snapshotList.add(recipeSnapshot);
        }
        for (int i = snapshotList.size() -1; i>=0; i--) {
            recipes.add(parseIndividualRecipe(snapshotList.get(i)));
        }
        return recipes;
    }


    private static Recipe parseIndividualRecipe(DataSnapshot recipeSnapshot){
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

        return (new Recipe(author, dishName, cuisine, prepTime, cookTime, servings, veg, vegan, glutenFree,
                ingredients, steps, photoPath, calories, views ));
    }



    public static ArrayList<Recipe> getRecipesPeopleIFollow(DataSnapshot snapshot, ArrayList<String> following){
        ArrayList<Recipe> recipes = new ArrayList<>();
        List<DataSnapshot> snapshotList = new ArrayList<>();

        for(DataSnapshot recipeSnapshot : snapshot.getChildren()) {
            snapshotList.add(recipeSnapshot);
        }
        for (int i = snapshotList.size() -1; i>=0; i--) {
            DataSnapshot recipeSnapshot = snapshotList.get(i);
            String person = recipeSnapshot.child("authorName").getValue(String.class);
            if (following.contains(person)){
                recipes.add(parseIndividualRecipe(snapshotList.get(i)));
            }

        }
        return recipes;
    }
    public static void fetchAllRecipes(ValueEventListener listener) {
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference().child("recipes");
        recipesRef.addListenerForSingleValueEvent(listener);
    }

    public static void fetchAllUsers(ValueEventListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addListenerForSingleValueEvent(listener);
    }

    public static void fetchRecipesByAuthor(ValueEventListener listener, String username) {
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference().child("recipes");
        Query query = recipesRef.orderByChild("authorName").equalTo(username);
        query.addListenerForSingleValueEvent(listener);
    }



    public static Bitmap downloadFoodPic(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();

        InputStream input = conn.getInputStream();
        return BitmapFactory.decodeStream(input);

    }

    public static String formatMessageTimeStamp(LocalDateTime dateTime) {
        String monthAndDate = dateTime.format(DateTimeFormatter.ofPattern("MMM d"));
        String time = dateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        return  monthAndDate + " • " + time;
    }
}
