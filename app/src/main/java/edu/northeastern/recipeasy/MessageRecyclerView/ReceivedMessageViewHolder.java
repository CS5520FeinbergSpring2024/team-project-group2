package edu.northeastern.recipeasy.MessageRecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.domain.Message;
import edu.northeastern.recipeasy.utils.DataUtil;

public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
    private TextView timeStampView;
    private TextView messageContentView;
    public ReceivedMessageViewHolder(@NonNull View itemView) {
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
