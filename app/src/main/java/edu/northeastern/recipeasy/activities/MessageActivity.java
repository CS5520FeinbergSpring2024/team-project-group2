package edu.northeastern.recipeasy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import edu.northeastern.recipeasy.MessageRecyclerView.MessageViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Message;

public class MessageActivity extends AppCompatActivity {
    private MessageViewAdapter messageAdapter;
    private RecyclerView messageRecycler;
    private TextView toolBarTextView;
    private String currentUsername;
    private String otherUsername;
    private ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

//        currentUsername = getIntent().getStringExtra("username");
//        messages = new ArrayList<>();
        addSampleData();
        currentUsername = "user1";
        otherUsername = "John Doe";
        toolBarTextView = findViewById(R.id.toolBarId);
        toolBarTextView.setText(otherUsername);

        messageRecycler = findViewById(R.id.messageRecyclerViewId);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageViewAdapter(this, currentUsername, messages);
        messageRecycler.setAdapter(messageAdapter);

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
}