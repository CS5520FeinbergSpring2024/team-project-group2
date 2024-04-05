package edu.northeastern.recipeasy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import edu.northeastern.recipeasy.MessageRecyclerView.MessageViewAdapter;
import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Message;

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

//        currentUsername = getIntent().getStringExtra("username");
//        messages = new ArrayList<>();
        if (savedInstanceState != null) {
            this.messages = savedInstanceState.getParcelableArrayList(MESSAGES_KEY);
        } else {
            addSampleData();
        }

        currentUsername = "user1";
        otherUsername = "John Doe";
        toolBarTextView = findViewById(R.id.toolBarId);
        newMessageContentTextview = findViewById(R.id.messageTextInputId);
        toolBarTextView.setText(otherUsername);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageRecycler = findViewById(R.id.messageRecyclerViewId);
        messageRecycler.setLayoutManager(layoutManager);
        messageAdapter = new MessageViewAdapter(this, currentUsername, messages);
        messageRecycler.setAdapter(messageAdapter);

    }


    private void sendMessage() {
        String content = String.valueOf(newMessageContentTextview.getText());
        if (content.trim().equals("")) {
            return;
        }
        String sender = currentUsername;
        String receiver = otherUsername;
        Message message = new Message(sender, receiver, content);
        messages.add(message);
        newMessageContentTextview.setText("");
        this.messageAdapter.notifyItemInserted(messages.size() - 1);
        messageRecycler.scrollToPosition(messages.size() - 1);

        // close keyboard
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

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