package com.tsaravan9.myconciergeandroid.models;

import com.google.firebase.firestore.PropertyName;

public class Delivery {
    private String name;
    private String description;
    private Boolean visitor;
    private Boolean accepted;
    private Long enteredAt;

    public Delivery(String name, String description, Boolean isVisitor, Long enteredAt) {
        this.name = name;
        this.description = description;
        this.visitor = isVisitor;
        this.accepted = false;
        this.enteredAt = enteredAt;
    }

    public Delivery(){

    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
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
        return visitor;
    }

    public void setVisitor(Boolean visitor) {
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
                ", isVisitor=" + visitor +
                ", isAccepted=" + accepted +
                ", enteredAt=" + enteredAt +
                '}';
    }
}
