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
    private ArrayList<String> users;
    private Context context;
    private InboxItemClickListener listener;

    public InboxViewAdapter(ArrayList<String> userItemList, Context context) {
        this.users = userItemList;
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
        String user = users.get(position);
        holder.username.setText(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
