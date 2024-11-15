package com.bkimp.life.model;

import com.google.gson.JsonElement;

public class Category {
    private JsonElement en;
    private String iconName;
    private String category;

    // Constructor
    public Category(JsonElement en, String iconName, String category) {
        this.en = en;
        this.iconName = iconName;
        this.category = category;
    }

    // Getter for the en field
    public JsonElement getEn() {
        return en;
    }

    // Setter for the en field
    public void setEn(JsonElement en) {
        this.en = en;
    }

    // Getter for the iconName field
    public String getIconName() {
        return iconName;
    }

    // Setter for the iconName field
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    // Getter for the category field
    public String getCategory() {
        return category;
    }

    // Setter for the category field
    public void setCategory(String category) {
        this.category = category;
    }
}


