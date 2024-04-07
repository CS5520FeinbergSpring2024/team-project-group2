package edu.northeastern.recipeasy.Listeners;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.recipeasy.domain.Message;
import edu.northeastern.recipeasy.utils.DataUtil;

public class ConversationListener {
    private DatabaseReference conversationRef;
    private ValueEventListener listener;
    private ConversationUpdateListener conversationUpdater;

    public ConversationListener(ConversationUpdateListener conversationUpdater, String currentUsername, String otherUsername) {
        this.conversationUpdater = conversationUpdater;
        conversationRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUsername)
                .child("convos").child(otherUsername).child("messages");
        startListener();
    }

    private void startListener() {
        new Thread(() -> {
            try{
                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Should send a notification here
                        Log.w("CONVO UPDATE", dataSnapshot + "");
                        ArrayList<Message> receivedMessages = DataUtil.parseMessages(dataSnapshot);
                        if (conversationUpdater != null) {
                            conversationUpdater.onConversationUpdate(receivedMessages);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };

                conversationRef.addValueEventListener(listener);
            } catch (Exception e) {

            }
        }).start();
    }

    public void unregisterListener() {
        if (listener != null) {
            conversationRef.removeEventListener(listener);
        }
    }
}
