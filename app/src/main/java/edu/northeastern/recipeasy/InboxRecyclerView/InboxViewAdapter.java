package edu.northeastern.recipeasy.InboxRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.RecipeRecyclerView.RecipeViewHolder;
import edu.northeastern.recipeasy.UserRecyclerView.UserItemClickListener;
import edu.northeastern.recipeasy.UserRecyclerView.UserViewHolder;
import edu.northeastern.recipeasy.domain.User;

public class InboxViewAdapter extends RecyclerView.Adapter<InboxViewHolder>{
    private ArrayList<User> userItemList;
    private Context context;
    private InboxItemClickListener listener;

    public InboxViewAdapter(ArrayList<User> userItemList, Context context) {
        this.userItemList = userItemList;
        this.context = context;
    }

    public void setMessageClickListener(InboxItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_inbox, parent, false);
        return new InboxViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        User user = userItemList.get(position);
        holder.username.setText(user.getUsername());

    }

    @Override
    public int getItemCount() {
        return userItemList.size();
    }
}
