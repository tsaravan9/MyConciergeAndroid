package com.tsaravan9.myconciergeandroid.models;

import java.util.Date;

public class Text {
    private String sender;
    private String message;
    private Long sentAt;

    public Text(String sender, String message, Long sentAt) {
        this.sender = sender;
        this.message = message;
        this.sentAt = sentAt;
    }

    public Text(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSentAt() {
        return sentAt;
    }

    public void setSentAt(Long sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "Text{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
