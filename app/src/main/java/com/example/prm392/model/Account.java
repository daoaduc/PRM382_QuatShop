package com.example.prm392.model;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private int accID;
    private String fullname;
    private int gender;
    private String email;
    private String password;
    private String phoneNumber;
    private int roleID;
    private int status;
    private String profilePicture;
    private Date createAt;
    private Date updateAt;

    public Account(int accID, String fullname, int gender, String email, String password, String phoneNumber, int roleID, int status, String profilePicture, Date createAt, Date updateAt) {
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
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

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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