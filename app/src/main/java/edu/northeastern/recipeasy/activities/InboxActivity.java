package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Objects;

import edu.northeastern.recipeasy.InboxRecyclerView.InboxItemClickListener;
import edu.northeastern.recipeasy.InboxRecyclerView.InboxViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.UserRecyclerView.UserItemClickListener;
import edu.northeastern.recipeasy.UserRecyclerView.UserViewAdapter;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.utils.IUserFetchListener;
import edu.northeastern.recipeasy.utils.UserManager;

public class InboxActivity extends AppCompatActivity implements IUserFetchListener {

    private RecyclerView inboxRecyclerView;
    private InboxViewAdapter inboxAdapter;
    private String username;
    private ArrayList<User> userList;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        username = getIntent().getStringExtra("username");
        UserManager userManager = new UserManager();
        userManager.getUser(username, this);

        setUp();
    }

    public void setUp() {
        inboxRecyclerView = findViewById(R.id.inboxRecyclerView);
        inboxRecyclerView.setHasFixedSize(true);
        inboxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        inboxAdapter = new InboxViewAdapter(userList, this);
        InboxItemClickListener clickListener = position -> {
            User clickedUser = userList.get(position);
            Intent goMessage = new Intent(InboxActivity.this, MessageActivity.class);
            goMessage.putExtra("currentUsername", username);
            goMessage.putExtra("profileUsername", clickedUser.getUsername());
            startActivity(goMessage);
        };
        inboxAdapter.setMessageClickListener(clickListener);
        inboxRecyclerView.setAdapter(inboxAdapter);
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