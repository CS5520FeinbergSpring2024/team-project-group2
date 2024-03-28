package edu.northeastern.recipeasy.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {

    private String dishName;
    private String cuisine;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private boolean isVeg;
    private boolean isVegan;
    private boolean isGlutenFree;
    private String ingredients;
    private String steps;
    private String photoPath;
    private Integer calories;
    private String author;

    private Integer views;


    public Recipe(String author, String dishName, String cuisine, Integer prepTime,
                  Integer cookTime, Integer servings, boolean isVeg, boolean isVegan,
                  boolean isGlutenFree, String ingredients,
                  String steps, String photoPath, Integer calories, Integer views) {
        this.dishName = dishName;
        this.author = author;
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
        this.calories = calories;
        this.views = views;

    }

    public String getAuthorName() {
        return author;
    }

    public void setAuthorName(String authorName) {
        this.author = authorName;
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Integer getCalories() {
        return calories;
    }
    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }


}
