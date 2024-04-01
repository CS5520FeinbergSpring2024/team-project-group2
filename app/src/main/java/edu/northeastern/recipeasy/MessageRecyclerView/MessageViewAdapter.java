package edu.northeastern.recipeasy.MessageRecyclerView;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.recipeasy.domain.Message;
import edu.northeastern.recipeasy.domain.MessageType;

public class MessageViewAdapter extends RecyclerView.Adapter{
    private String currentUsername;
    private ArrayList<Message> messages;
    private Context context;

    public MessageViewAdapter(Context context, String currentUsername, ArrayList<Message> messages) {
        this.currentUsername = currentUsername;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private MessageType getMessageType(Message message) {
        if (message.getSenderUsername().equals(currentUsername)) {
            return MessageType.SENT;
        } else {
            return MessageType.RECEIVED;
        }
    }
}
