package com.tsaravan9.myconciergeandroid.models;

public class Delivery {
    private String name;
    private String description;
    private boolean visitor;
    private boolean allowed = false;
    private Long enteredAt;
    private boolean rejected = false;

    public Delivery(String name, String description, boolean isVisitor, Long enteredAt) {
        this.name = name;
        this.description = description;
        this.visitor = isVisitor;
        this.enteredAt = enteredAt;
    }

    public Delivery(){

    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
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

    public boolean getVisitor() {
        return visitor;
    }

    public void setVisitor(boolean visitor) {
        this.visitor = visitor;
    }

    public Long getEnteredAt() {
        return enteredAt;
    }

    public void setEnteredAt(Long enteredAt) {
        this.enteredAt = enteredAt;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", visitor=" + visitor +
                ", accepted=" + allowed +
                ", enteredAt=" + enteredAt +
                ", rejected=" + rejected +
                '}';
    }
}
