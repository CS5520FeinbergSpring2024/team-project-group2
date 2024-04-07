package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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
    private int position;
    public ImageView vegIcon;
    public ImageView veganIcon;
    public ImageView gfIcon;

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
        TextView cuisineTextView = findViewById(R.id.cuisineLabelId);

        vegIcon = findViewById(R.id.vegIcon);
        veganIcon = findViewById(R.id.veganIcon);
        gfIcon = findViewById(R.id.gfIcon);

        if (!recipe.isGlutenFree()){
            gfIcon.setVisibility(View.GONE);
        }if (!recipe.isVeg()){
            vegIcon.setVisibility(View.GONE);
        }if (!recipe.isVegan()){
            veganIcon.setVisibility(View.GONE);
        }

        tabs.post(() -> tabs.getTabAt(position).select());

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

        String ingredientsData = recipe.getIngredients();
        if (ingredientsData != null) {
            String[] ingredientsArray = ingredientsData.split(Pattern.quote(";;"));
            for (int i = 0; i < ingredientsArray.length; i++) {
                ingredientsArray[i] = "• " + ingredientsArray[i];
            }
            ingredientsData = String.join("\n\n", ingredientsArray);
        } else {
            ingredientsData = "";
        }
        String stepsData = String.join("\n\n", recipe.getSteps().split(Pattern.quote(";;")));

        recipeNameTextView.setText(recipe.getDishName());
        authorTextView.setText("By: " + recipe.getAuthorName());
        if (recipe.getCalories() == 0) {
            caloriesTextView.setText("Calories: N/A");
        } else {
            caloriesTextView.setText("Calories: " + recipe.getCalories().toString() + " Calories");
        }
        if (recipe.getCookTime() == 0) {
            prepTimeTextView.setText("Prep/Cook time: " +  "N/A /" + recipe.getCookTime().toString() + " minutes");
        } else if (recipe.getPrepTime() == 0) {
            prepTimeTextView.setText("Prep/Cook time: " + recipe.getPrepTime().toString() + "/ N/A");

        } else {
            prepTimeTextView.setText("Prep/Cook Time (mins): " + recipe.getPrepTime().toString() + "/" + recipe.getCookTime().toString());
        }

        if(recipe.getServings() == 0) {
            servingsTextView.setText("Servings: N/A");
        } else {
            servingsTextView.setText("Servings: " + recipe.getServings().toString());
        }

        if(recipe.getCuisine() == "") {
            cuisineTextView.setText("Cuisine: N/A");
        }else {
            cuisineTextView.setText("Cuisine: " + recipe.getCuisine().toString());
        }

        longContextTextView.setText(ingredientsData);

        String finalIngredientsData = ingredientsData;
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (position == 0) {
                    longContextTextView.setText(finalIngredientsData);
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
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("position");
    }
}