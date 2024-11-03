package com.example.prm392.model;

import java.net.Socket;
import java.util.List;

public class ChatRoom {
    String roomName;
    List<Socket> members;

    // constructor
    public ChatRoom(){}
    public ChatRoom(List<Socket> members) {
        this.members = members;
    }
    public ChatRoom(String roomName, List<Socket> members) {
        this.roomName = roomName;
        this.members = members;
    }
    // getters and setters
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
