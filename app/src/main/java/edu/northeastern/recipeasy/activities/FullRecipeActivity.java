package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Recipe;
public class FullRecipeActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe);

        recipe = new Recipe(
                "Spaghetti Carbonara",
                "John Doe",
                "Italian",
                20,
                30,
                4,
                false,
                false,
                false,
                "200g spaghetti\n100g pancetta\n2 eggs\n50g grated Parmesan cheese\nsalt\npepper",
                "1. Cook spaghetti according to package instructions.\n2. In a pan, fry pancetta until crispy.\n3. In a bowl, whisk together eggs and Parmesan cheese.\n4. Drain spaghetti and add to the pan with pancetta.\n5. Remove from heat and quickly stir in egg mixture.n6. Season with salt and pepper to taste. Serve immediately.",
                "/path/to/photo.jpg",
                450,
                10,
                100
        );

        TabLayout tabs = findViewById(R.id.tabView);
        TextView longContextTextView = findViewById(R.id.longContentTextId);
        TextView recipeNameTextView = findViewById(R.id.recipeNameId);
        TextView authorTextView = findViewById(R.id.authorLabelId);
        TextView caloriesTextView = findViewById(R.id.caloriesLabelId);
        TextView prepTimeTextView = findViewById(R.id.prepTimeLabelId);
        TextView servingsTextView = findViewById(R.id.servingsLabelId);

        // TODO handle null values
        recipeNameTextView.setText(recipe.getDishName());
        authorTextView.setText(recipe.getAuthorName());
        caloriesTextView.setText(recipe.getCalories().toString());
        prepTimeTextView.setText(recipe.getPrepTime().toString());
        servingsTextView.setText(recipe.getServings().toString());
        longContextTextView.setText(recipe.getIngredients());

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    longContextTextView.setText(recipe.getIngredients());
                } else if (position == 1) {
                    longContextTextView.setText(recipe.getSteps());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}