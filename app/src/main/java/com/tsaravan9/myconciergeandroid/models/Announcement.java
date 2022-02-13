package com.tsaravan9.myconciergeandroid.models;

public class Announcement {
    private String title;
    private String description;
    private Long postedAt;

    public Announcement(String title, String description, Long postedAt) {
        this.title = title;
        this.description = description;
        this.postedAt = postedAt;
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

    public Long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Long postedAt) {
        this.postedAt = postedAt;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", postedAt=" + postedAt +
                '}';
    }
}
