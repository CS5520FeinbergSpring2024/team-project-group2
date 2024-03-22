package edu.northeastern.recipeasy.ItemRecyvlerView;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.recipeasy.R;

import java.util.List;

public class ListItemViewAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    private List<ListItem> listItems;


    public ListItemViewAdapter(List<ListItem> listItems) {
        this.listItems = listItems;
    }


    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.item.setText(listItem.getItem());
        holder.listItemNumber.setText(listItem.getLabel()+" " +(position + 1) +": ");
        holder.item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listItem.setItem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (position > 0 && position == listItems.size() -1 ) {
            holder.item.requestFocus();
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
