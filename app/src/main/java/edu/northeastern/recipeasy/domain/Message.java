package edu.northeastern.recipeasy.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class Message {
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private ZonedDateTime timeStamp;

    public Message(String senderUsername, String receiverUsername, String message) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.message = message;
        ZoneId zone = TimeZone.getDefault().toZoneId();
        this.timeStamp = ZonedDateTime.now(zone);
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

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
