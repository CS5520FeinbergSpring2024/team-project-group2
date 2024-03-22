package edu.northeastern.recipeasy.domain;

public class Message {
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private String timeStamp;

    public Message(String senderUsername, String receiverUsername, String message, String timeStamp) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.message = message;
        this.timeStamp = timeStamp;
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
}
