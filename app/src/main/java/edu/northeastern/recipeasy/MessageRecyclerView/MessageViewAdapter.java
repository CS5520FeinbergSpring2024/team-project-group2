package edu.northeastern.recipeasy.MessageRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewHolder;
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
        if (viewType == MessageType.SENT.getValue()) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_card, parent, false);
            return new SentMessageViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message_card, parent, false);
            return new ReceivedMessageViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getItemViewType() == MessageType.SENT.getValue()) {
            ((SentMessageViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderUsername().equals(currentUsername)) {
            return MessageType.SENT.getValue();
        } else {
            return MessageType.RECEIVED.getValue();
        }
    }

}
