package edu.northeastern.recipeasy.utils;

import com.google.firebase.database.DatabaseError;

import edu.northeastern.recipeasy.domain.User;

public interface IUserFetchListener {
    void onUserFetched(User user);
    void onError(DatabaseError databaseError);
}