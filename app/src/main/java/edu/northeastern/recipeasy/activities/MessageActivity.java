package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

import edu.northeastern.recipeasy.Listeners.ConversationListener;
import edu.northeastern.recipeasy.Listeners.ConversationUpdateListener;
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
        ConversationUpdateListener conversationUpdateListener = messagesList -> {
            messages.clear();
            messages.addAll(messagesList);
            messageAdapter.notifyDataSetChanged();
            if (messages.size() > 0) {
                messageRecycler.scrollToPosition(messages.size() - 1);
            }
        };
        ConversationListener conversationListener = new ConversationListener(conversationUpdateListener, currentUsername, otherUsername);

        // query for otherUser in conversations node of currentUser
        // if doesn't exist, create it in currentUser and otherUser
        // if it does, pull the list of messages into messages arraylist


        initializeRecycler();

        toolBarTextView = findViewById(R.id.toolBarId);
        newMessageContentTextview = findViewById(R.id.messageTextInputId);
        toolBarTextView.setText(otherUsername.toUpperCase());
    }

    private void initializeRecycler() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageRecycler = findViewById(R.id.messageRecyclerViewId);
        messageRecycler.setLayoutManager(layoutManager);
        messageAdapter = new MessageViewAdapter(this, currentUsername, messages);
        messageRecycler.setAdapter(messageAdapter);
        initializeMessageList();
    }

    private void initializeMessageList() {
        new Thread(()->{
            try{
                // Get the reference to the "convos" node
                DatabaseReference convosRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUsername).child("convos");

                // Check if "otherUsername" exists under "convos"
                convosRef.child(otherUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // "otherUsername" exists, populate the arraylist of messages
                            DataSnapshot otherUserMessagesSnapshot = dataSnapshot.child("messages");
                            ArrayList<Message> messagesList = DataUtil.parseMessages(otherUserMessagesSnapshot);

                            messages.clear();
                            messages.addAll(messagesList);
                            messageAdapter.notifyDataSetChanged();
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

            } catch (Exception e){

            }
        }).start();

    }

    private void sendMessage() {
        String content = String.valueOf(newMessageContentTextview.getText());
        if (content.trim().equals("")) {
            return;
        }
        String sender = currentUsername;
        String receiver = otherUsername;
        Message message = new Message(sender, receiver, content, false);

        addMessageToDB(currentUsername, otherUsername, message);
        addMessageToDB(otherUsername, currentUsername, message);
        addReceivedToDB(message);

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

    private void addReceivedToDB(Message message) {
        new Thread(() -> {
            DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference().child("messages").child(message.getReceiverUsername());
            receiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DatabaseReference newMessageRef = receiverRef.push();
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

    @Override
    public void onClick(View v) {
        int clickedId = v.getId();
        if (clickedId == R.id.sendMessageButton) {
            sendMessage();
        }
    }
}