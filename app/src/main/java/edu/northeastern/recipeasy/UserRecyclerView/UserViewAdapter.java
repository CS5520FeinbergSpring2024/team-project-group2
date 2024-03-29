package edu.northeastern.recipeasy.UserRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.User;


public class UserViewAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private ArrayList<User> userItemList;
    private Context context;

    public UserViewAdapter(ArrayList<User> userItemList, Context context) {
        this.userItemList = userItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userItemList.get(position);
        holder.username.setText(user.getUsername());

    }

    @Override
    public int getItemCount() {
        return userItemList.size();
    }
}
