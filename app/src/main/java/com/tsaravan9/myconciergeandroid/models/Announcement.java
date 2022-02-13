package com.tsaravan9.myconciergeandroid.models;

public class Announcement {
    private String title;
    private String description;

    public Announcement(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Announcement(){

    }

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
}
