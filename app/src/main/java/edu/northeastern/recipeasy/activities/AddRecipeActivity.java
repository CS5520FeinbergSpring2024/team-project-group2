package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.slider.RangeSlider;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.recipeasy.ItemRecyvlerView.ListItem;
import edu.northeastern.recipeasy.ItemRecyvlerView.ListItemViewAdapter;
import edu.northeastern.recipeasy.R;

public class AddRecipeActivity extends AppCompatActivity {
    private RecyclerView ingredientsRecyclerView;
    private ArrayList<ListItem> ingredientList;
    private ListItemViewAdapter ingredientAdapter;
    private RecyclerView recipeRecyclerView;
    private ArrayList<ListItem> recipeStepsList;
    private ListItemViewAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        initializeIngredientsRecycler();
        initializeRecipeRecycler();
    }


    private void initializeIngredientsRecycler(){
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerViewId);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientList = new ArrayList<>();
        ingredientList.add(new ListItem("", "Ingredient"));
        ingredientAdapter = new ListItemViewAdapter(ingredientList);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
    }
    private void initializeRecipeRecycler(){
        recipeRecyclerView = findViewById(R.id.recipeRecyclerViewId);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeStepsList = new ArrayList<>();
        recipeStepsList.add(new ListItem("", "Step"));
        recipeAdapter = new ListItemViewAdapter(recipeStepsList);
        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    public void clickedAddNewStep(View view){
        int clickedId = view.getId();
        if (clickedId == R.id.addNewIngredientButton) {
            int position = ingredientList.size() - 1;
            ListItem lastItem = ingredientList.get(position);
            if (!lastItem.getItem().trim().isEmpty()) {
                ingredientList.add(new ListItem("", "Ingredient"));
                position = ingredientList.size() - 1;
                ingredientAdapter.notifyItemInserted(position);
                ingredientsRecyclerView.scrollToPosition(position);
            } else {
                blankListItem();
            }
        } else {
            int position = recipeStepsList.size() - 1;
            ListItem lastItem = recipeStepsList.get(position);
            if (!lastItem.getItem().trim().isEmpty()) {
                recipeStepsList.add(new ListItem("", "Step"));
                position = recipeStepsList.size() - 1;
                recipeAdapter.notifyItemInserted(position);
                recipeRecyclerView.scrollToPosition(position);
            } else {
                blankListItem();
            }
        }
    }

    private void blankListItem() {
        Toast.makeText(this, "Step cannot be left blank", Toast.LENGTH_SHORT).show();
    }

    public void clickedCamera(View view) {
    }

    public void clickedGallery(View view) {
    }

    public void submitRecipe(View view) {
        //TODO: make a recipe object and push to DB

        String[] formedStrings = formListItemStrings();

        EditText dish = findViewById(R.id.editDishAddRecipe);
        RangeSlider calorieSlider = findViewById(R.id.rangeSliderCaloriesIdAddNewRecipe);
        EditText cookTime = findViewById(R.id.cookTimeId);
        EditText prepTime = findViewById(R.id.prepTimeId);
        EditText serving = findViewById(R.id.servingSizeIdAddNewRecipe);
        Spinner cuisineSpinner = findViewById(R.id.spinnerAddNewCuisine);

        String dishName = dish.getText().toString();
        Log.w(" DISHNAME", " " + dishName);
        // String author = username;
        String ingredients =  formedStrings[0];
        Log.w("INGREDIENTS", " " +ingredients );
        int calories = Math.round(calorieSlider.getValues().get(0));
        Log.w("CALORIES", " " + calories);
        int cookTimeMinutes = 0;
        if (!cookTime.getText().toString().isEmpty()) {
            cookTimeMinutes = Integer.parseInt(cookTime.getText().toString());
        }
        Log.w("COOK TIME", " " + cookTimeMinutes);

        int prepTimeMinutes = 0;
        if (!prepTime.getText().toString().isEmpty()) {
            prepTimeMinutes = Integer.parseInt(prepTime.getText().toString());
        }
        Log.w("PREP TIME", " " + prepTimeMinutes);

        //TODO: decide how we want to do dietary. 3 separate booleans? a list?

        String recipeSteps = formedStrings[1];
        int servingSizes = 0;
        if (!serving.getText().toString().isEmpty()) {
            servingSizes = Integer.parseInt(serving.getText().toString());
        }
        Log.w("SERVINGS", " " + servingSizes );

        //TODO: figure out how to store a picture
        String cuisine = cuisineSpinner.getSelectedItem().toString();

        // likes = 0
        // dislikes = 0

        Log.w(" CUISINE", " " + cuisine );


    }

    private String[] formListItemStrings() {
        StringBuilder ingredients = new StringBuilder();
        StringBuilder recipeSteps = new StringBuilder();

        for (int i = 0; i < ingredientList.size(); i++) {
            ListItem item = ingredientList.get(i);
            ingredients.append(item.getItem()).append("\n");
        }
        for (int i = 0; i < recipeStepsList.size(); i++) {
            ListItem stepItem = recipeStepsList.get(i);
            recipeSteps.append(stepItem.getLabel())
                    .append(" ").append(i+1).append(": ")
                    .append(stepItem.getItem()).append("\n");
        }
        return new String[]{ingredients.toString(), recipeSteps.toString()};
    }
}