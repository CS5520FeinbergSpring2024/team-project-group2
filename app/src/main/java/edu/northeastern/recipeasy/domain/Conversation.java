package edu.northeastern.recipeasy.domain;

import java.util.ArrayList;

public class Conversation {

    private String userOne;
    private String userTwo;
    private ArrayList<Message> messages;

    public Conversation(String userOne, String userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.messages = new ArrayList<>();
    }

    public String getUserOne() {
        return userOne;
    }

    public void setUserOne(String userOne) {
        this.userOne = userOne;
    }

    public String getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(String userTwo) {
        this.userTwo = userTwo;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

}
