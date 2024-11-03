package com.example.prm392.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private int roomID;
    private Account sender;
    private String message;

    public Message() {
    }

    public Message(int roomID, String message) {
        this.roomID = roomID;
        this.message = message;
    }
    public Message(int roomID, Account sender, String message) {
        this.roomID = roomID;
        this.sender = sender;
        this.message = message;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Account getSender() {
        return sender;
    }
    public void setSender(Account sender) {
        this.sender = sender;
    }
}
