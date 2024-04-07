package edu.northeastern.recipeasy.domain;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> messages;

    public Conversation() {

        this.messages = new ArrayList<>();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

}
