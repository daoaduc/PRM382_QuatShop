package com.example.prm392.model;

import java.io.Serializable;
import java.net.Socket;
import java.util.List;

public class ChatRoom implements Serializable {
    private int roomID;
    private String roomName;
    private List<Socket> members;

    // constructor
    public ChatRoom(){}
    public ChatRoom(int roomID, String roomName, List<Socket> members) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.members = members;
    }
    public ChatRoom(int roomID, String roomName) {
        this.roomID = roomID;
        this.roomName = roomName;
    }
    public ChatRoom(List<Socket> members) {
        this.members = members;
    }
    public ChatRoom(String roomName, List<Socket> members) {
        this.roomName = roomName;
        this.members = members;
    }
    // getters and setters
    public int getRoomID() {
        return roomID;
    }
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public List<Socket> getMembers() {
        return members;
    }
    public void setMembers(List<Socket> members) {
        this.members = members;
    }
    // add member
    public void addMember(Socket member) {
        members.add(member);
    }
}
