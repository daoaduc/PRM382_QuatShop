package com.example.prm392.model;
public class AccountRole {
    private int roleID;
    private String role;
    public AccountRole() {
    }
    public AccountRole(int roleID) {
        this.roleID = roleID;
    }
    public AccountRole(int roleID, String role) {
        this.roleID = roleID;
        this.role = role;
    }
    public int getRoleID() {
        return roleID;
    }
    public String getRole() {
        return role;
    }
    @Override
    public String toString() {
        return role;
    }
}
