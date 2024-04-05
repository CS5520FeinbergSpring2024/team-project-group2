package edu.northeastern.recipeasy.InboxRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.recipeasy.R;


public class InboxViewHolder extends RecyclerView.ViewHolder{

    public Button viewMessages;
    public TextView username;
    public InboxViewHolder(@NonNull View itemView, final InboxItemClickListener listener) {
        super(itemView);
        viewMessages = itemView.findViewById(R.id.seeMessagesID);
        username = itemView.findViewById(R.id.usernameID);

        viewMessages.setOnClickListener(v -> {
            if (listener != null) {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION) {

                    listener.onMessageClick(position);
                }
            }
        });
    }




}
