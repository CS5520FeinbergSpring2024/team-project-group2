package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private Button followUser;
    private Button unfollowUser;
    private Button messageUser;


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
        name.setText(profileUsername.toUpperCase());
        setUp();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        menu = bottomNavigationView.getMenu();
        profileMenuItem = menu.findItem(R.id.profile_icon);
        profileMenuItem.setChecked(true);

    }

    private void setUpPage(){
        followUser = findViewById(R.id.followUserButtonId);
        unfollowUser = findViewById(R.id.unfollowUserButtonId);
        messageUser = findViewById(R.id.messageUserButtonId);
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
                unfollowUser.setVisibility(View.VISIBLE);
            } else {
                unfollowUser.setVisibility(View.GONE);
                followUser.setVisibility(View.VISIBLE);
            }
            // profile user is a follower of current user
            if ((user.getFollowers().contains(profileUsername) && user.getFollowing().contains(profileUsername))){
                messageUser.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
            } else{

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
            goHome.putExtra("username", currentUsername);
            startActivity(goHome);
            return true;
        } else if(itemId == R.id.search_icon) {
//            Toast.makeText(this, "SEARCH", Toast.LENGTH_LONG).show();
            Intent goSearch = new Intent(ProfileActivity.this, SearchActivity.class);
            goSearch.putExtra("username", user.getUsername());
            startActivity(goSearch);
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

    // TODO threading
    @Override
    public void onClick(View v) {
        int clickedId = v.getId();
        if (clickedId == R.id.seeFollowingButtonId){
            handleSeeFollowers(R.id.seeFollowingButtonId);
        } else if (clickedId == R.id.seeFollowersButtonId){
            handleSeeFollowers(R.id.seeFollowersButtonId);
        } else if (clickedId == R.id.followUserButtonId){
            updateMyFollowingListFollow();
            updateTheirFollowersListFollow();
        } else if (clickedId == R.id.unfollowUserButtonId){
            updateMyFollowingListUnfollow();
            updateTheirFollowersListUnfollow();
        } else if (clickedId == R.id.messageUserButtonId){
            if ((user.getFollowers().contains(profileUsername) && user.getFollowing().contains(profileUsername))){
                // Create conversation
                Intent conversationActivity = new Intent(ProfileActivity.this, MessageActivity.class);
                conversationActivity.putExtra("currentUsername", user.getUsername());
                conversationActivity.putExtra("otherUsername", profileUsername);
                startActivity(conversationActivity);
            } else{
                Toast.makeText(this, "You can't message "+ profileUsername +"\n"
                        + "You must mutually follow!", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void handleSeeFollowers(int clickedId) {
        Intent expandUsers = new Intent(ProfileActivity.this, SeeFollowersFollowingActivity.class);
//        expandUsers.putExtra("user", user);
        if (clickedId == R.id.seeFollowingButtonId){
            expandUsers.putExtra("type", "Following");
            expandUsers.putStringArrayListExtra("list", user.getFollowing());
        } else {
            expandUsers.putExtra("type", "Followers");
            expandUsers.putStringArrayListExtra("list", user.getFollowers());
        }
        expandUsers.putExtra("currentUsername", currentUsername);
        startActivity(expandUsers);

    }

    private void updateMyFollowingListFollow() {
        DatabaseReference profileUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUsername).child("following");
        profileUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> followList = DataUtil.parseStringArray(dataSnapshot);
                followList.add(profileUsername);
                user.getFollowing().add(profileUsername);
                profileUserRef.setValue(followList).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setUpPage();
                        Toast.makeText(ProfileActivity.this,"You followed "+profileUsername+"!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void updateTheirFollowersListFollow() {
        DatabaseReference profileUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(profileUsername).child("followers");
        profileUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> followList = DataUtil.parseStringArray(dataSnapshot);
                followList.add(currentUsername);
                profileUserRef.setValue(followList).addOnCompleteListener(task -> {
                    setUpPage();
                    Log.w("FOLLOWERS UPDATED", "updated " +profileUsername);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateMyFollowingListUnfollow() {
        DatabaseReference profileUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUsername).child("following");
        profileUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> followList = DataUtil.parseStringArray(dataSnapshot);
                followList.remove(profileUsername);
                user.getFollowing().remove(profileUsername);
                profileUserRef.setValue(followList).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setUpPage();
                        Toast.makeText(ProfileActivity.this,"You unfollowed "+profileUsername+"!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void updateTheirFollowersListUnfollow() {
        DatabaseReference profileUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(profileUsername).child("followers");
        profileUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> followList = DataUtil.parseStringArray(dataSnapshot);
                followList.remove(currentUsername);
                profileUserRef.setValue(followList).addOnCompleteListener(task -> {
                    setUpPage();
                    Log.w("FOLLOWERS UPDATED", "updated " +profileUsername);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}