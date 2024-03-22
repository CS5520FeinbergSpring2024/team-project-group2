package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        initializeIngredientsRecycler();
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
        }
    }

    private void blankListItem() {
        Toast.makeText(this, "Step cannot be left blank", Toast.LENGTH_SHORT).show();
    }
}