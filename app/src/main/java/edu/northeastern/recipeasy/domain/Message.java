package edu.northeastern.recipeasy.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

import edu.northeastern.recipeasy.utils.DataUtil;

public class Message implements Parcelable {
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private String timeStamp;
    private boolean sentNotification;

    public Message(String senderUsername, String receiverUsername, String message, boolean sentNotification) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.message = message;
        this.sentNotification = sentNotification;

        ZoneId zone = TimeZone.getDefault().toZoneId();
        this.timeStamp = DataUtil.zonedDatetimeToString(ZonedDateTime.now(zone));
    }

    public Message(String senderUsername, String receiverUsername, String message, boolean sentNotification, String timeStamp) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.message = message;
        this.timeStamp = timeStamp;
        this.sentNotification = sentNotification;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    // to store using onSaveInstanceState
    protected Message(Parcel in) {
        senderUsername = in.readString();
        receiverUsername = in.readString();
        message = in.readString();
        timeStamp = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(senderUsername);
        dest.writeString(receiverUsername);
        dest.writeString(message);
        dest.writeString(timeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSentNotification() {
        return sentNotification;
    }

    public void setSentNotification(boolean sentNotification) {
        this.sentNotification = sentNotification;
    }
}