package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import edu.northeastern.recipeasy.InboxRecyclerView.InboxItemClickListener;
import edu.northeastern.recipeasy.InboxRecyclerView.InboxViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewAdapter;
import edu.northeastern.recipeasy.UserRecyclerView.UserItemClickListener;
import edu.northeastern.recipeasy.UserRecyclerView.UserViewAdapter;
import edu.northeastern.recipeasy.domain.Conversation;
import edu.northeastern.recipeasy.domain.Message;
import edu.northeastern.recipeasy.domain.User;
import edu.northeastern.recipeasy.utils.DataUtil;
import edu.northeastern.recipeasy.utils.IUserFetchListener;
import edu.northeastern.recipeasy.utils.UserManager;

public class InboxActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private RecyclerView inboxRecyclerView;
    private InboxViewAdapter inboxAdapter;
    private String username;
    private ArrayList<String> userList = new ArrayList<>();
    private Menu menu;
    private MenuItem messageIcon;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        username = getIntent().getStringExtra("username");
        bottomNavigationView = findViewById(R.id.bottom_navigationInbox);
        bottomNavigationView.setOnItemSelectedListener(this);
        menu = bottomNavigationView.getMenu();
        messageIcon = menu.findItem(R.id.message_icon);
        messageIcon.setChecked(true);

        initializeConversationList();
    }

    public void setUp() {
        inboxRecyclerView = findViewById(R.id.inboxRecyclerView);
        inboxRecyclerView.setHasFixedSize(true);
        inboxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inboxAdapter = new InboxViewAdapter(userList, this);
        InboxItemClickListener clickListener = position -> {
            String clickedUser = userList.get(position);
            Intent goMessage = new Intent(InboxActivity.this, MessageActivity.class);
            goMessage.putExtra("currentUsername", username);
            goMessage.putExtra("otherUsername", clickedUser);
            startActivity(goMessage);
        };
        inboxAdapter.setMessageClickListener(clickListener);
        inboxRecyclerView.setAdapter(inboxAdapter);
    }

    private void initializeConversationList() {
        // Get the reference to the "convos" node
        DatabaseReference convosRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("convos");
        // Check if "otherUsername" exists under "convos"
        convosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w("convos", dataSnapshot + "");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    userList.add(child.getKey());
                }

                // initialize recyclerview
                setUp();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home_icon) {
            Intent goHome = new Intent(InboxActivity.this, HomePage.class);
            goHome.putExtra("username", username);
            startActivity(goHome);
            return true;
        } else if(itemId == R.id.search_icon) {
            Intent goSearch = new Intent(InboxActivity.this, SearchActivity.class);
            goSearch.putExtra("username", username);
            startActivity(goSearch);
            return true;
        }else if(itemId == R.id.profile_icon) {
            Intent goProfile = new Intent(InboxActivity.this, ProfileActivity.class);
            goProfile.putExtra("currentUsername", username);
            goProfile.putExtra("profileUsername", username);
            goProfile.putExtra("isCurrentUser", true);
            startActivity(goProfile);
            return true;
        }
        return false;
    }
}