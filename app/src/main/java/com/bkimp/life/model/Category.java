package com.bkimp.life.model;

import java.util.Map;

import java.util.Map;

public class Category {
    private Map<String, Disease> en;  // Map of disease names to Disease objects
    private String iconName;
    private String category;

    // Constructor
    public Category(Map<String, Disease> en, String iconName, String category) {
        this.en = en;
        this.iconName = iconName;
        this.category = category;
    }

    // Getter for the en field
    public Map<String, Disease> getEn() {
        return en;
    }

    // Setter for the en field
    public void setEn(Map<String, Disease> en) {
        this.en = en;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
