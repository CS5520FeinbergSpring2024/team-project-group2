package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.UserRecyclerView.UserViewAdapter;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.utils.DataUtil;
import edu.northeastern.recipeasy.utils.IUserFetchListener;
import edu.northeastern.recipeasy.utils.UserManager;

public class SearchActivity extends AppCompatActivity implements IUserFetchListener, NavigationBarView.OnItemSelectedListener {
    private RecyclerView userRecyclerView;
    private UserViewAdapter userAdapter;
    private RecipeViewAdapter recipeAdapter;
    private ArrayList<User> userList;
    private ArrayList<Recipe> recipeList;
    private RecyclerView recipeRecyclerView;
    private User user;
    private TabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String username = getIntent().getStringExtra("username");
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);

        tabs = findViewById(R.id.tabLayoutID);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
                    setUpUserRecyclerView();
                } else if (position == 1) {
                    Toast.makeText(getApplicationContext(), "Recipes", Toast.LENGTH_SHORT).show();
                    setUpRecipeRecyclerView();

                }

                else if (position == 2) {
                    Toast.makeText(getApplicationContext(), "API", Toast.LENGTH_SHORT).show();

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

    public void setUpUserRecyclerView() {
        userRecyclerView = findViewById(R.id.userRecyclerID);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserViewAdapter(userList, this);
        userRecyclerView.setAdapter(userAdapter);
        loadAllUsers();
    }

    public void setUpRecipeRecyclerView() {
        recipeRecyclerView = findViewById(R.id.userRecyclerID);
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeViewAdapter(recipeList, this);
        recipeRecyclerView.setAdapter(recipeAdapter);
        loadAllRecipes();
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

    private void loadAllUsers(){
        new Thread(() -> {
            DataUtil.fetchAllUsers(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<User> users = DataUtil.parseUsers(dataSnapshot);
                    userList.clear();
                    userList.addAll(users);
                    new Handler(Looper.getMainLooper()).post(() -> userAdapter.notifyDataSetChanged());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int selectedTabPosition = tabs.getSelectedTabPosition();
        if (selectedTabPosition == 0) {
            setUpUserRecyclerView();
        } else if (selectedTabPosition == 1) {
            setUpRecipeRecyclerView();
            Toast.makeText(getApplicationContext(), "Recipes", Toast.LENGTH_SHORT).show();

        } else if (selectedTabPosition == 2) {
            Toast.makeText(getApplicationContext(), "API", Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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