package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.recipeasy.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAddRecipeActivity(View view){
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int clickedId = view.getId();
        if (clickedId == R.id.homeID) {
            Intent HomePageActivity = new Intent(this, HomePage.class);
            startActivity(HomePageActivity);
        }
    }
}