package com.example.prm392.DAO;

import android.util.Log;
import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
    ConnectionClass connectionClass;

    public AccountDAO() {
        connectionClass = new ConnectionClass();
    }

    // Check if an email already exists
    public boolean emailExists(String email) {
        boolean exists = false;
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "SELECT * FROM `account` WHERE `email` = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                exists = resultSet.next();
                resultSet.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exists;
    }

    // Save a new user to the database
    public void saveUserToDatabase(Account account) {
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "INSERT INTO `account` (`fullname`, `email`, `password`, `roleID`, `status`) VALUES (?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, account.getFullname());
                preparedStatement.setString(2, account.getEmail());
                preparedStatement.setString(3, account.getPassword());
                preparedStatement.setInt(4, account.getRoleID());
                preparedStatement.setInt(5, account.getStatus());

                preparedStatement.executeUpdate();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Update account status
    public void updateAccountStatus(String email) {
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "UPDATE `account` SET `status` = 1 WHERE `email` = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
