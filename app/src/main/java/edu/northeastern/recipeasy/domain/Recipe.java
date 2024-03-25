package edu.northeastern.recipeasy.domain;

import java.util.ArrayList;

public class Recipe {

    private String dishName;
    private String cuisine;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private boolean isVeg;
    private boolean isVegan;
    private boolean isGlutenFree;
    private ArrayList<String> ingredients;
    private ArrayList<String> steps;
    private String photoPath;

    public Recipe(String dishName, String cuisine, Integer prepTime,
                  Integer cookTime, Integer servings, boolean isVeg, boolean isVegan,
                  boolean isGlutenFree, ArrayList<String> ingredients,
                  ArrayList<String> steps, String photoPath) {
        this.dishName = dishName;
        this.cuisine = cuisine;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.isVeg = isVeg;
        this.isVegan = isVegan;
        this.isGlutenFree = isGlutenFree;
        this.ingredients = ingredients;
        this.steps = steps;
        this.photoPath = photoPath;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean veg) {
        isVeg = veg;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
