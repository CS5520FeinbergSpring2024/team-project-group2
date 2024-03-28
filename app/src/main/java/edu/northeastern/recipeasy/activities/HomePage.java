package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.utils.IUserFetchListener;
import edu.northeastern.recipeasy.utils.UserManager;

public class HomePage extends AppCompatActivity implements IUserFetchListener {

    private RecyclerView recipeRecyclerView;
    private RecipeViewAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // TO THIS FIRST
        String username = getIntent().getStringExtra("username");
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);
    }

    public void setUp() {
        recipeRecyclerView = findViewById(R.id.recyclerID);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeViewAdapter(recipeList, this);
        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onUserFetched(User user) {
        if (user != null) {
            this.user = user;
            for (String follower : user.getFollowers()) {
                Log.d("User", follower);
            }
        }
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Log.d("User", "User not found");
    }
}