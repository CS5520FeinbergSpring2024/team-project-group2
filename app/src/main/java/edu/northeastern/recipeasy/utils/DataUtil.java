package edu.northeastern.recipeasy.utils;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class DataUtil {

    public static ArrayList<String> parseStringArray(DataSnapshot snapshot) {
        ArrayList<String> items = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            String item = childSnapshot.getValue(String.class);
            items.add(item);
        }
        return items;
    }
}
