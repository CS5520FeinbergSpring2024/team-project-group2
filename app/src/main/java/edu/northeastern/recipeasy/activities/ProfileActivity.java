package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.utils.DataUtil;
import edu.northeastern.recipeasy.utils.IUserFetchListener;
import edu.northeastern.recipeasy.utils.UserManager;

public class ProfileActivity extends AppCompatActivity implements IUserFetchListener {

    private RecyclerView recipeRecyclerView;
    private RecipeViewAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;
    private User user;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        username = getIntent().getStringExtra("username");
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);
        TextView name = findViewById(R.id.nameID);
        name.setText(username);
        setUp();
    }

    public void setUp() {
        recipeRecyclerView = findViewById(R.id.recyclerID);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeViewAdapter(recipeList, this);
        recipeRecyclerView.setAdapter(recipeAdapter);

        loadMyRecipes();
    }

    public void loadMyRecipes() {
        new Thread(() -> {
            DataUtil.fetchRecipesByAuthor(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Recipe> recipes = DataUtil.parseRecipes(dataSnapshot);
                    recipeList.clear();
                    recipeList.addAll(recipes);
                    new Handler(Looper.getMainLooper()).post(() -> recipeAdapter.notifyDataSetChanged());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            }, username);
        }).start();
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