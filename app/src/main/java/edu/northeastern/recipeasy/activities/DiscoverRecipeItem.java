package edu.northeastern.recipeasy.activities;

public class DiscoverRecipeItem {
    private String dishName;
    private String imageUrl;
    private int calories;
    private String recipeUrl;
    private String description;

    public DiscoverRecipeItem(String dishName, String imageUrl, int calories, String recipeUrl, String description) {
        this.dishName = dishName;
        this.imageUrl = imageUrl;
        this.calories = calories;
        this.recipeUrl = recipeUrl;
        this.description = description;
    }

    public String getDishName() {
        return dishName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCalories() {
        return calories;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public String getDescription() {
        return description;
    }
}
