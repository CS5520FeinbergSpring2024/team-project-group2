package edu.northeastern.recipeasy.MessageRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Message;
import edu.northeastern.recipeasy.utils.DataUtil;

public class SentMessageViewHolder extends RecyclerView.ViewHolder{
    private TextView timeStampView;
    private TextView messageContentView;

    public SentMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        timeStampView = itemView.findViewById(R.id.dateTimeViewId);
        messageContentView = itemView.findViewById(R.id.messageContentId);
    }

    public void bind(Message message) {
        String dateText = DataUtil.formatMessageTimeStamp(DataUtil.stringToZonedDateTime(message.getTimeStamp()).toLocalDateTime());
        timeStampView.setText(dateText);
        messageContentView.setText(message.getMessage());
    }
}
