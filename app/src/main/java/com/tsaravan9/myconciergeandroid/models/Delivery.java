package com.tsaravan9.myconciergeandroid.models;

public class Delivery {
    private String name;
    private String description;
    private Boolean isVisitor;
    private Boolean isAccepted;

    public Delivery(String name, String description, Boolean isVisitor) {
        this.name = name;
        this.description = description;
        this.isVisitor = isVisitor;
        this.isAccepted = false;
    }

    public Delivery(){

    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVisitor() {
        return isVisitor;
    }

    public void setVisitor(Boolean visitor) {
        isVisitor = visitor;
    }
}
