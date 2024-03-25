package edu.northeastern.recipeasy.domain;

public class ListItem {
    private String item;
    private String label;



    public ListItem(String item, String label) {

        this.item = item;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
