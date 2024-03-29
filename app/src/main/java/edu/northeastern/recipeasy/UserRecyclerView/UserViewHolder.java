package edu.northeastern.recipeasy.UserRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.recipeasy.R;

public class UserViewHolder extends RecyclerView.ViewHolder{

    public Button viewProfile;
    public TextView username;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        viewProfile = itemView.findViewById(R.id.viewProfileButtonID);
        username = itemView.findViewById(R.id.usernameID);
    }
}
