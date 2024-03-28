package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase database;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("users");
    }

    @Override
    public void onClick(View view) {
        EditText usernameField = findViewById(R.id.usernameTextID);
        String username = usernameField.getText().toString().toLowerCase();
        if (username.trim().equals("")) {
            Toast.makeText(this, "Invalid Username", Toast.LENGTH_LONG).show();
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        Executors.newSingleThreadExecutor().execute(() -> userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(username)) {
                    handler.post(() -> startNextActivity(username));
                } else {

                    Toast.makeText(MainActivity.this, "New account created!", Toast.LENGTH_LONG).show();
                    User user = new User(username);
                    ArrayList<String> followers = new ArrayList<>();
                    followers.add("jerry");
                    followers.add("chloe");
                    user.setFollowers(followers);
                    ArrayList<String> following = new ArrayList<>();
                    following.add("bram");
                    following.add("chloe");
                    user.setFollowing(following);
                    userRef.child(username).setValue(user).addOnSuccessListener(
                            (task) -> handler.post(() -> startNextActivity(username)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }));
    }

    public void startNextActivity(String username) {
        Intent homePageIntent = new Intent(MainActivity.this, HomePage.class);
        homePageIntent.putExtra("username", username);
        startActivity(homePageIntent);
    }

    public static class ProfileActivity {
    }
}