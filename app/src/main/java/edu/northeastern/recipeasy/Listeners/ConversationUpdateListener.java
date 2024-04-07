package edu.northeastern.recipeasy.Listeners;

import java.util.ArrayList;

import edu.northeastern.recipeasy.domain.Message;

public interface ConversationUpdateListener {
    void onConversationUpdate(ArrayList<Message> messages);
}