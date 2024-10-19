package com.example.prm392.model;

public class ProductStatus {
    private int statusID;
    private String statusName;

    public ProductStatus() {
    }

    public ProductStatus(int statusID) {
        this.statusID = statusID;
    }

    public ProductStatus(int statusID, String statusName) {
        this.statusID = statusID;
        this.statusName = statusName;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
