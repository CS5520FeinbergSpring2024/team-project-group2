package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.regex.Pattern;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.utils.DataUtil;
public class FullRecipeActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        TabLayout tabs = findViewById(R.id.tabView);
        TextView longContextTextView = findViewById(R.id.longContentTextId);
        TextView recipeNameTextView = findViewById(R.id.recipeNameId);
        TextView authorTextView = findViewById(R.id.authorLabelId);
        TextView caloriesTextView = findViewById(R.id.caloriesLabelId);
        TextView prepTimeTextView = findViewById(R.id.prepTimeLabelId);
        TextView servingsTextView = findViewById(R.id.servingsLabelId);
        ImageView foodPicture = findViewById(R.id.imageView);
        foodPicture.setImageResource(R.drawable.no_image);

        new Thread(() -> {
            try {
                Bitmap picBitMap = DataUtil.downloadFoodPic(recipe.getPhotoPath());

                if(! recipe.getPhotoPath().equals("")){
                    if (picBitMap != null) {
                        foodPicture.post(() -> foodPicture.setImageBitmap(picBitMap));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        String ingredientsData = String.join("\n", recipe.getIngredients().split(Pattern.quote(";;")));
        String stepsData = String.join("\n", recipe.getSteps().split(Pattern.quote(";;")));

        // TODO handle null values
        recipeNameTextView.setText(recipe.getDishName());
        authorTextView.setText("By " + recipe.getAuthorName());
        caloriesTextView.setText(recipe.getCalories().toString() + " Calories");
        prepTimeTextView.setText("Prep time: " + recipe.getPrepTime().toString() + " minutes");
        servingsTextView.setText("Servings: " + recipe.getServings().toString());
        longContextTextView.setText(ingredientsData);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    longContextTextView.setText(ingredientsData);
                } else if (position == 1) {
                    longContextTextView.setText(stepsData);
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