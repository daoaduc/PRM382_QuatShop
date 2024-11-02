package com.example.prm392.model;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private int accID;
    private String fullname;
    private boolean gender;
    private String email;
    private String password;
    private String phoneNumber;
    private AccountRole roleID;
    private AccountStatus status;
    private String profilePicture;
    private Date createAt;
    private Date updateAt;

    // Constructors
    public Account(int accID, String fullname, boolean gender, String email, String phoneNumber, AccountRole roleID, AccountStatus status, String profilePicture, Date createAt, Date updateAt) {
        this.accID = accID;
        this.fullname = fullname;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
        this.status = status;
        this.profilePicture = profilePicture;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    // Constructors
    public Account(int accID,String fullname, boolean gender, String email, String password, String phoneNumber, AccountRole roleID, AccountStatus status, String profilePicture, Date createAt, Date updateAt) {
        this.accID = accID;
        this.fullname = fullname;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
        this.status = status;
        this.profilePicture = profilePicture;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    public Account() {}

    public Account(int accID) {
        this.accID = accID;
    }

    // Getters and setters
    public int getAccID() {
        return accID;
    }

    public void setAccID(int accID) {
        this.accID = accID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AccountRole getRoleID() {
        return roleID;
    }

    public void setRoleID(AccountRole roleID) {
        this.roleID = roleID;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }


    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accID=" + accID +
                ", fullname='" + fullname + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roleID=" + roleID +
                ", status=" + status +
                ", profilePicture='" + profilePicture + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
