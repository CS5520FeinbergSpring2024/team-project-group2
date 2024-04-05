package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import edu.northeastern.recipeasy.MessageRecyclerView.MessageViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Message;
import edu.northeastern.recipeasy.utils.DataUtil;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private MessageViewAdapter messageAdapter;
    private RecyclerView messageRecycler;
    private LinearLayoutManager layoutManager;
    private TextView toolBarTextView;
    private EditText newMessageContentTextview;
    private String currentUsername;
    private String otherUsername;
    private ArrayList<Message> messages = new ArrayList<>();
    private static final String MESSAGES_KEY = "messages_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        otherUsername = getIntent().getStringExtra("otherUsername");
        currentUsername = getIntent().getStringExtra("currentUsername");

        // query for otherUser in conversations node of currentUser
        // if doesn't exist, create it in currentUser and otherUser
        // if it does, pull the list of messages into messages arraylist

//        if (savedInstanceState != null) {
//            this.messages = savedInstanceState.getParcelableArrayList(MESSAGES_KEY);
//        } else {
//            initializeMessageList();
//        }

        initializeMessageList();
        Log.w("Convo", "Message List Initialized");

        toolBarTextView = findViewById(R.id.toolBarId);
        newMessageContentTextview = findViewById(R.id.messageTextInputId);
        toolBarTextView.setText(otherUsername);
    }

    private void onMessageSnapshotReceived() {
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setStackFromEnd(true);
            messageRecycler = findViewById(R.id.messageRecyclerViewId);
            messageRecycler.setLayoutManager(layoutManager);
            messageAdapter = new MessageViewAdapter(this, currentUsername, messages);
            messageRecycler.setAdapter(messageAdapter);
            Log.w("Convo", "Recycler done");
    }

    // TODO threading
    private void initializeMessageList() {
        // Get the reference to the "convos" node
        DatabaseReference convosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUsername).child("convos");

        // Check if "otherUsername" exists under "convos"
        convosRef.child(otherUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // "otherUsername" exists, populate the arraylist of messages
                    DataSnapshot otherUserMessagesSnapshot = dataSnapshot.child("messages");
                    messages = DataUtil.parseMessages(otherUserMessagesSnapshot);
                    Log.w("Convo", otherUserMessagesSnapshot + "");

                    // initialize recyclerview
                    onMessageSnapshotReceived();

//                    messages.clear();
//                    messages.addAll(messagesList);
//                    messageAdapter.notifyDataSetChanged();
                } else {
                    // "otherUsername" doesn't exist, create it
                    convosRef.child(otherUsername).setValue(true);
                    convosRef.child(otherUsername).child("messages").setValue(new ArrayList<Message>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void sendMessage() {
        String content = String.valueOf(newMessageContentTextview.getText());
        if (content.trim().equals("")) {
            return;
        }
        String sender = currentUsername;
        String receiver = otherUsername;
        Message message = new Message(sender, receiver, content);

        addMessageToDB(currentUsername, otherUsername, message);
        addMessageToDB(otherUsername, currentUsername, message);

        messages.add(message);
        newMessageContentTextview.setText("");
        this.messageAdapter.notifyItemInserted(messages.size() - 1);
        messageRecycler.scrollToPosition(messages.size() - 1);

        // close keyboard (reference: https://stackoverflow.com/questions/13032436/hiding-softkeyboard-reliably)
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    private void addMessageToDB(String user1, String user2, Message message) {
        new Thread(() -> {
            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("users").child(user1).child("convos").child(user2).child("messages");
            messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DatabaseReference newMessageRef = messagesRef.push();
                    newMessageRef.setValue(message);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(MESSAGES_KEY, this.messages);
        super.onSaveInstanceState(outState);
    }


    private void addSampleData() {
        String user1 = "user1";
        String user2 = "user2";

        // Sending messages from user1 to user2
        messages.add(new Message(user1, user2, "Hello, User2!"));
        messages.add(new Message(user1, user2, "How are you?"));
        messages.add(new Message(user1, user2, "I hope you're having a great day!"));
        messages.add(new Message(user1, user2, "Did you see the latest episode of that show?"));
        messages.add(new Message(user1, user2, "I can't wait for our upcoming event!"));

        // Sending messages from user2 to user1
        messages.add(new Message(user2, user1, "Hi, User1! I'm doing well, thank you."));
        messages.add(new Message(user2, user1, "What about you?"));
        messages.add(new Message(user2, user1, "Yes, I watched it. It was amazing!"));
        messages.add(new Message(user2, user1, "Me too! It's going to be so much fun."));
        messages.add(new Message(user2, user1, "I'll see you there!"));

        messages.add(new Message(user2, user1, "Hi, User1! I'm doing well, thank you."));
        messages.add(new Message(user2, user1, "What about you?"));
        messages.add(new Message(user2, user1, "Yes, I watched it. It was amazing!"));
        messages.add(new Message(user2, user1, "Me too! It's going to be so much fun."));
        messages.add(new Message(user2, user1, "I'll see you there!"));

        messages.add(new Message(user2, user1, "Hi, User1! I'm doing well, thank you."));
        messages.add(new Message(user2, user1, "What about you?"));
        messages.add(new Message(user2, user1, "Yes, I watched it. It was amazing!"));
        messages.add(new Message(user2, user1, "Me too! It's going to be so much fun."));
        messages.add(new Message(user2, user1, "I'll see you there!"));

        // Shuffle the messages
        Collections.shuffle(messages);
    }

    @Override
    public void onClick(View v) {
        int clickedId = v.getId();
        if (clickedId == R.id.sendMessageButton) {
            sendMessage();
        }
    }
}