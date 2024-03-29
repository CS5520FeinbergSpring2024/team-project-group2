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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.utils.DataUtil;
import edu.northeastern.recipeasy.utils.IUserFetchListener;
import edu.northeastern.recipeasy.utils.UserManager;

public class HomePage extends AppCompatActivity implements IUserFetchListener, NavigationBarView.OnItemSelectedListener {

    private RecyclerView recipeRecyclerView;
    private RecipeViewAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;
    private User user;
    private TabLayout tabs;
    private Menu menu;
    private MenuItem homeIcon;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        menu = bottomNavigationView.getMenu();
        homeIcon = menu.findItem(R.id.home_icon);


        tabs = findViewById(R.id.tabViewHomePage);

        setUp();
        // DO THIS FIRST
        String username = getIntent().getStringExtra("username");
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);
        FloatingActionButton addNewRecipe = findViewById(R.id.fabID);
        addNewRecipeButtonListener(addNewRecipe);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    loadAllRecipes();
                } else if (position == 1) {
                    loadFollowingRecipes();
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
    protected void onResume() {
        super.onResume();

        homeIcon.setChecked(true);

        int selectedTabPosition = tabs.getSelectedTabPosition();
        if (selectedTabPosition == 0) {
            loadAllRecipes();
        } else if (selectedTabPosition == 1) {
            loadFollowingRecipes();
        }
    }



    private void loadAllRecipes(){
        new Thread(() -> {
            DataUtil.fetchAllRecipes(new ValueEventListener() {
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
            });
        }).start();
    }

    private void loadFollowingRecipes(){
        new Thread(() -> {
            DataUtil.fetchAllRecipes(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Recipe> recipes = DataUtil.getRecipesPeopleIFollow(dataSnapshot, user.getFollowing());
                    recipeList.clear();
                    recipeList.addAll(recipes);
                    new Handler(Looper.getMainLooper()).post(() -> recipeAdapter.notifyDataSetChanged());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }).start();
    }

    private void addNewRecipeButtonListener(FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            Intent newRecipeIntent = new Intent(HomePage.this, AddRecipeActivity.class);
            newRecipeIntent.putExtra("username", user.getUsername());
            startActivity(newRecipeIntent);
        });
    }

    public void setUp() {
        recipeRecyclerView = findViewById(R.id.recyclerID);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeViewAdapter(recipeList, this);
        recipeRecyclerView.setAdapter(recipeAdapter);
        loadAllRecipes();
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
        int itemId = item.getItemId();
        if(itemId == R.id.search_icon) {
            Toast.makeText(this, "SEARCH", Toast.LENGTH_LONG).show();
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
        } else if(itemId == R.id.profile_icon) {
            Intent goProfile = new Intent(HomePage.this, ProfileActivity.class);
            goProfile.putExtra("currentUsername", user.getUsername());
            goProfile.putExtra("profileUsername", "test");
            goProfile.putExtra("isCurrentUser", true);
            startActivity(goProfile);
            return true;
        }

        return false;
    }
}