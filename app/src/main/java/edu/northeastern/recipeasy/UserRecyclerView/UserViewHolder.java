package edu.northeastern.recipeasy.UserRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.User;

public class UserViewHolder extends RecyclerView.ViewHolder{

    public Button viewProfile;
    public TextView username;

    public UserViewHolder(@NonNull View itemView, final UserItemClickListener listener) {
        super(itemView);

        viewProfile = itemView.findViewById(R.id.viewProfileButtonID);
        username = itemView.findViewById(R.id.usernameID);

        viewProfile.setOnClickListener(v -> {
            if (listener != null) {
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION) {

                    listener.onViewProfileClick(position);
                }
            }
        });
    }
}
