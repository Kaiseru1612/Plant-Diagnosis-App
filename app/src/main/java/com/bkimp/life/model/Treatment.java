package com.bkimp.life.model;

public static class Treatment {
    private String title;
    private String description;
    private String shortContent;
    private String longContent;

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public String getLongContent() {
        return longContent;
    }

    public void setLongContent(String longContent) {
        this.longContent = longContent;
    }
}
