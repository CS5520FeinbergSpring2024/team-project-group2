package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class InboxActivity extends AppCompatActivity {

    private RecyclerView inboxRecyclerView;
    private InboxViewAdapter inboxAdapter;
    private String username;
    private ArrayList<String> userList = new ArrayList<>();
    private ArrayList<Conversation> conversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        username = getIntent().getStringExtra("username");

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
}