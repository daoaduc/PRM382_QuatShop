package com.example.prm392.model;

public class OrderStatus {
    private int statusID;
    private String status;

    public OrderStatus() {

    }

    public OrderStatus(int statusID) {
        this.statusID = statusID;
    }

    public OrderStatus(int statusID, String status) {
        this.statusID = statusID;
        this.status = status;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "statusID=" + statusID +
                ", status='" + status + '\'' +
                '}';
    }
}
