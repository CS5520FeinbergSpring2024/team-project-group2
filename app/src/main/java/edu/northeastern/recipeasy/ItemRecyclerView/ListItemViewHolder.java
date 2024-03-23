package edu.northeastern.recipeasy.ItemRecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.recipeasy.R;

public class ListItemViewHolder extends RecyclerView.ViewHolder{
    public TextView listItemNumber;
    public EditText item;

    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);

        listItemNumber = itemView.findViewById(R.id.listItemLabelId);
        item = itemView.findViewById(R.id.editListItemId);

    }
}