package com.bkimp.life.model;

public class TreatmentDetails {
    private String longContent;
    private String shortContent;
    private String title;

    public String getLongContent() {
        return longContent;
    }

    public void setLongContent(String longContent) {
        this.longContent = longContent;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TreatmentDetail{" +
                "longContent='" + longContent + '\'' +
                ", shortContent='" + shortContent + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}