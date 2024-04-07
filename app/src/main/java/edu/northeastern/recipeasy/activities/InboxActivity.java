package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.recipeasy.InboxRecyclerView.InboxItemClickListener;
import edu.northeastern.recipeasy.InboxRecyclerView.InboxViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Recipe;
import edu.northeastern.recipeasy.utils.DataUtil;

public class InboxActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private RecyclerView inboxRecyclerView;
    private InboxViewAdapter inboxAdapter;
    private String username;
    private ArrayList<String> userList = new ArrayList<>();
    private ArrayList<String> originalUserList = new ArrayList<>();
    private Menu menu;
    private MenuItem messageIcon;
    private BottomNavigationView bottomNavigationView;
    private SearchView search;
    private String filterText = "";
    private SwipeRefreshLayout swipeRefreshLayout;




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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


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

        setupSearch();
    }

    private void initializeConversationList() {
        // Get the reference to the "convos" node
        DatabaseReference convosRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("convos");
        // Check if "otherUsername" exists under "convos"
        convosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    userList.add(child.getKey());
                    originalUserList.add(child.getKey());
                }

                // initialize recyclerview
                setUp();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setupSearch() {
        search = findViewById(R.id.searchBarID);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterInboxList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterInboxList(newText);
                filterText = newText;
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadConversations();
        });
    }

    private void loadConversations(){
        new Thread(() -> {
            DatabaseReference convosRef = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("convos");
            convosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList.clear();
                    originalUserList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        userList.add(child.getKey());
                        originalUserList.add(child.getKey());
                    }
                    new Handler(Looper.getMainLooper()).post(() -> inboxAdapter.notifyDataSetChanged());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }

    private void filterInboxList(String text){
        ArrayList<String> filteredList = new ArrayList<>();
        for (String u : originalUserList) {
            if (u.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(u);
            }
        }
        if (userList != null) {
            userList.clear();
            userList.addAll(filteredList);
            inboxAdapter.notifyDataSetChanged();
        }
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