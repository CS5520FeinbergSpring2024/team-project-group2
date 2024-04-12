package edu.northeastern.recipeasy.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.recipeasy.Listeners.NotificationListener;
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
    private RecyclerView recipeRecyclerFollowingView;
    private RecipeViewAdapter recipeFollowingAdapter;
    private ArrayList<Recipe> recipeFollowingList = new ArrayList<>();
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private User user;
    private TabLayout tabs;
    private Menu menu;
    private MenuItem homeIcon;
    private BottomNavigationView bottomNavigationView;
    private static final int NOTIFICATION_REQUEST_CODE = 101;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // DO THIS FIRST
        String username = getIntent().getStringExtra("username");
        Boolean fromLogin = getIntent().getBooleanExtra("fromLogIn", false);
        // register notification listener
        NotificationListener notificationListener = new NotificationListener(username, this);
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);
        FloatingActionButton addNewRecipe = findViewById(R.id.fabID);
        addNewRecipeButtonListener(addNewRecipe);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fromLogin){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                    builder.setTitle("Go back?");
                    builder.setMessage("Going back to the login screen will log you out.\nAre you sure you want to log out?");
                    builder.setPositiveButton("YES", (dialog, which) -> logout());
                    builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
                    builder.show();
                } else{
                    finish();
                }
            }
        });


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutHome);
        menu = bottomNavigationView.getMenu();
        homeIcon = menu.findItem(R.id.home_icon);
        tabs = findViewById(R.id.tabViewHomePage);
        tabs.post(() -> tabs.getTabAt(position).select());
        setUp();


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (position == 0) {
                    setUp();
                } else if (position == 1) {
                    setUpFollowing();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (position == 0) {
                setUp();
            } else if (position == 1) {
                setUpFollowing();
            }
        });
    }

    public void logout() {
        // finish activities and open login page
        finishAffinity();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeIcon.setChecked(true);
        int selectedTabPosition = tabs.getSelectedTabPosition();
        if (selectedTabPosition == 0) {
            setUp();
        } else if (selectedTabPosition == 1) {
            setUpFollowing();
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
                    recipeFollowingList.clear();
                    recipeFollowingList.addAll(recipes);
                    new Handler(Looper.getMainLooper()).post(() -> recipeFollowingAdapter.notifyDataSetChanged());
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
        recipeAdapter = new RecipeViewAdapter(recipeList, this);
        recipeRecyclerView.setAdapter(recipeAdapter);
        loadAllRecipes();
    }

    public void setUpFollowing() {
        recipeRecyclerFollowingView = findViewById(R.id.recyclerID);
        recipeRecyclerFollowingView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeFollowingAdapter = new RecipeViewAdapter(recipeFollowingList, this);
        recipeRecyclerFollowingView.setAdapter(recipeFollowingAdapter);
        loadFollowingRecipes();
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
            Intent goSearch = new Intent(HomePage.this, SearchActivity.class);
            goSearch.putExtra("username", user.getUsername());
            startActivity(goSearch);
            return true;
        }else if(itemId == R.id.message_icon) {
            Intent goMessages = new Intent(HomePage.this, InboxActivity.class);
            goMessages.putExtra("username", user.getUsername());
            startActivity(goMessages);
            return true;
        } else if(itemId == R.id.profile_icon) {
            Intent goProfile = new Intent(HomePage.this, ProfileActivity.class);
            goProfile.putExtra("currentUsername", user.getUsername());
            goProfile.putExtra("profileUsername", user.getUsername());
            goProfile.putExtra("isCurrentUser", true);
            startActivity(goProfile);
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification Permission Required", Toast.LENGTH_LONG).show();
            }
        }
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