package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.UserRecyclerView.UserItemClickListener;
import edu.northeastern.recipeasy.UserRecyclerView.UserViewAdapter;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.utils.DataUtil;

public class SeeFollowersFollowingActivity extends AppCompatActivity {

    private UserViewAdapter userAdapter;
    private ArrayList<User> userList;
    private RecyclerView userRecyclerView;
    private User user;
    private ArrayList<String> userIds;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_followers_following);
        userIds = getIntent().getStringArrayListExtra("list");
        String type = getIntent().getStringExtra("type");
        currentUsername = getIntent().getStringExtra("currentUsername");
        TextView heading = findViewById(R.id.followersFollowingLabelId);
        heading.setText(type);

        setUpUserRecyclerView();


        loadUsersInList();
    }



    public void setUpUserRecyclerView() {
        userRecyclerView = findViewById(R.id.followersFollowingRecyclerId);
        userRecyclerView.setHasFixedSize(true);
        userList = new ArrayList<>();
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserViewAdapter(userList, this);
        UserItemClickListener clickListener = position -> {
            User clickedUser = userList.get(position);
            Intent goProfile = new Intent(SeeFollowersFollowingActivity.this, ProfileActivity.class);
            goProfile.putExtra("currentUsername", currentUsername);
            goProfile.putExtra("profileUsername", clickedUser.getUsername());
            goProfile.putExtra("isCurrentUser", Objects.equals(currentUsername, clickedUser.getUsername()));
            startActivity(goProfile);
        };
        userAdapter.setOnUserClickListener(clickListener);
        userRecyclerView.setAdapter(userAdapter);
        loadUsersInList();
    }


    private void loadUsersInList(){
        new Thread(() -> {
            DataUtil.fetchAllUsers(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<User> users = DataUtil.fetchUsersInList(dataSnapshot, userIds);
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

}