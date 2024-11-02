package com.example.prm392.model;
public class AccountStatus {
    private int statusID;
    private int statusName;
    public AccountStatus() {
    }
    public AccountStatus(int statusID) {
        this.statusID = statusID;
    }
    public AccountStatus(int statusID, int statusName) {
        this.statusID = statusID;
        this.statusName = statusName;
    }
    public int getStatusID() {
        return statusID;
    }
    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }
    public int getStatusName() {
        return statusName;
    }
    public void setStatusName(int statusName) {
        this.statusName = statusName;
    }
    @Override
    public String toString() {
        return statusName + "";
    }
}