package com.example.prm392.model;

public class Chat {
    private int chatID;
    private String userName;
    private String lastMessage;
    private String createdDate;

    // constructor
    public Chat(int chatID, String userName, String lastMessage, String createdDate) {
        this.chatID = chatID;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.createdDate = createdDate;
    }
    public Chat(String userName, String lastMessage){
        this.userName = userName;
        this.lastMessage = lastMessage;
    }

    // getter
    public int getChatID() {
        return chatID;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

}
