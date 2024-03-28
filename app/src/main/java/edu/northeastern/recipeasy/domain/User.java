package edu.northeastern.recipeasy.domain;

import java.util.ArrayList;

public class User {
    private String username;
    private ArrayList<String> recipeIdList = new ArrayList<>();
    private ArrayList<String> following = new ArrayList<>();
    private ArrayList<String> followers = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getRecipeIdList() {
        return recipeIdList;
    }

    public void setRecipeIdList(ArrayList<String> recipeIdList) {
        this.recipeIdList = recipeIdList;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }
}
