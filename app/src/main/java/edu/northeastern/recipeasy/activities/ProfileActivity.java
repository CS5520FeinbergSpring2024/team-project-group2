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
import android.widget.Button;
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

public class ProfileActivity extends AppCompatActivity implements IUserFetchListener,
        NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    private RecyclerView recipeRecyclerView;
    private RecipeViewAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;
    private boolean isCurrentUser;
    private User user;
    private String profileUsername;
    private String currentUsername;
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private MenuItem profileMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        profileUsername = getIntent().getStringExtra("profileUsername");
        currentUsername = getIntent().getStringExtra("currentUsername");
        isCurrentUser = getIntent().getBooleanExtra("isCurrentUser", false);
        UserManager userManager = new UserManager();
        userManager.getUser(currentUsername, this);
        TextView name = findViewById(R.id.nameID);
        name.setText(profileUsername);
        setUp();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        menu = bottomNavigationView.getMenu();
        profileMenuItem = menu.findItem(R.id.profile_icon);
        profileMenuItem.setChecked(true);

    }

    private void setUpPage(){
        Button followUser = findViewById(R.id.followUserButtonId);
        Button unfollowUser = findViewById(R.id.unfollowUserButtonId);
        Button messageUser = findViewById(R.id.messageUserButtonId);
        Button seeFollowing = findViewById(R.id.seeFollowingButtonId);
        Button seeFollowers = findViewById(R.id.seeFollowersButtonId);
        TextView reviewsHeading = findViewById(R.id.userReviewLabelId);
        if (isCurrentUser){
            followUser.setVisibility(View.GONE);
            unfollowUser.setVisibility(View.GONE);
            messageUser.setVisibility(View.GONE);
           reviewsHeading.setText("My Reviews:");
        } else{
            reviewsHeading.setText(profileUsername+ "'s Reviews:");
            seeFollowing.setVisibility(View.GONE);
            seeFollowers.setVisibility(View.GONE);

            // current user is a follower
            if (user.getFollowing().contains(profileUsername)){
                followUser.setVisibility(View.GONE);
            } else {
                unfollowUser.setVisibility(View.GONE);
            }
            // profile user is a follower of current user
            if (!user.getFollowers().contains(profileUsername)){
                messageUser.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
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
            }, profileUsername);
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profileMenuItem.setChecked(true);
    }

    @Override
    public void onUserFetched(User user) {
        if (user != null) {
            this.user = user;

            setUpPage();
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
            goHome.putExtra("username", profileUsername);
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

    @Override
    public void onClick(View v) {
        int clickedId = v.getId();
        if (clickedId == R.id.seeFollowingButtonId){
            Toast.makeText(this, "following", Toast.LENGTH_LONG).show();
        } else if (clickedId == R.id.seeFollowersButtonId){
            Toast.makeText(this, "followers", Toast.LENGTH_LONG).show();
        } else if (clickedId == R.id.followUserButtonId){
            Toast.makeText(this, "FOLLOW", Toast.LENGTH_LONG).show();
        } else if (clickedId == R.id.unfollowUserButtonId){
            Toast.makeText(this, "UNFOLLOW :(", Toast.LENGTH_LONG).show();
        } else if (clickedId == R.id.messageUserButtonId){
            if (!user.getFollowers().contains(profileUsername)){
                Toast.makeText(this, "You can't message "+ profileUsername +"\n" +
                        profileUsername +" does not follow you!", Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(this, "Message", Toast.LENGTH_LONG).show();
            }
        }
    }
}