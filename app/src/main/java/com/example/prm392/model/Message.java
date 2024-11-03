package com.example.prm392.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private Account sender;
    private Account receiver;
    private String content;
    private Date sendDate;
    private boolean isRead;

    // Default constructor
    public Message() {
    }

    // Constructor

    // Constructor
    public Message(Account sender, Account receiver, String content, Date sendDate, boolean isRead) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendDate = sendDate;
        this.isRead = isRead;
    }

    // Getters
    public Account getSender() {
        return sender;
    }
    public Account getReceiver() {
        return receiver;
    }
    public String getContent() {
        return content;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public boolean getIsRead() {
        return isRead;
    }

    // Setters
    public void setSender(Account sender) {
        this.sender = sender;
    }
    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
