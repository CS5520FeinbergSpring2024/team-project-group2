package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import edu.northeastern.recipeasy.ItemRecyvlerView.ListItem;
import edu.northeastern.recipeasy.ItemRecyvlerView.ListItemViewAdapter;
import edu.northeastern.recipeasy.R;

public class AddRecipeActivity extends AppCompatActivity {
    private RecyclerView ingredientsRecyclerView;
    private ArrayList<ListItem> ingredientList;
    private ListItemViewAdapter ingredientAdapter;
    private RecyclerView recipeRecyclerView;
    private ArrayList<ListItem> recipeList;
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
        recipeList = new ArrayList<>();
        recipeList.add(new ListItem("", "Step"));
        recipeAdapter = new ListItemViewAdapter(recipeList);
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
            int position = recipeList.size() - 1;
            ListItem lastItem = recipeList.get(position);
            if (!lastItem.getItem().trim().isEmpty()) {
                recipeList.add(new ListItem("", "Step"));
                position = recipeList.size() - 1;
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
    }
}