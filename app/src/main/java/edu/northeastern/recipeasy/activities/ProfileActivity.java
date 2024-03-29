package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

public class ProfileActivity extends AppCompatActivity implements IUserFetchListener, NavigationBarView.OnItemSelectedListener {

    private RecyclerView recipeRecyclerView;
    private RecipeViewAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;
    private boolean isCurrentUser;
    private User user;
    private String username;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        username = getIntent().getStringExtra("username");
        isCurrentUser = getIntent().getBooleanExtra("isCurrentUser", false);
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);
        TextView name = findViewById(R.id.nameID);
        name.setText(username);
        if (isCurrentUser){
            name.setText(username+" CURRENT USER");
        }
        setUp();

        bottomNavigationView.setSelectedItemId(R.id.profile_icon);
    }

    public void setUp() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

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
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.profile_icon);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.w("NAV ITEM SELECTED", ""+ item.getItemId() + " "+R.id.home_icon + " "+ R.id.profile_icon);
        int itemId = item.getItemId();
        if(itemId == R.id.home_icon) {
            Intent goHome = new Intent(ProfileActivity.this, HomePage.class);
            goHome.putExtra("username", username);
            startActivity(goHome);
            return true;
        } else if(itemId == R.id.search_icon) {
//            Toast.makeText(this, "SEARCH", Toast.LENGTH_LONG).show();
//            Intent goSearch = new Intent(HomePage.this, SearchActivity.class);
//            goSearch.putExtra("username", user.getUsername());
//            startActivity(goSearch);
            return true;
        }else if(itemId == R.id.message_icon) {
            Toast.makeText(this, "MESSAGES", Toast.LENGTH_LONG).show();
//            Intent goMessages = new Intent(HomePage.this, MessageActivity.class);
//            goMessages.putExtra("username", user.getUsername());
//            startActivity(goMessages);
            return true;
        }
        return false;
    }
}