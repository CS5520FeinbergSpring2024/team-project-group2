package edu.northeastern.recipeasy.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.recipeasy.domain.User;

public class UserManager {

    public void getUser(String username, IUserFetchListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference().child("users");

        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    ArrayList<String> recipeIdList = DataUtil.parseStringArray(dataSnapshot.child("recipeIdList"));
                    ArrayList<String> followers = DataUtil.parseStringArray(dataSnapshot.child("followers"));
                    ArrayList<String> following = DataUtil.parseStringArray(dataSnapshot.child("following"));

                    User user = new User(username);
                    user.setRecipeIdList(recipeIdList);
                    user.setFollowers(followers);
                    user.setFollowing(following);

                    listener.onUserFetched(user);
                } else {
                    // User does not exist
                    listener.onUserFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError);
            }
        });
    }
}