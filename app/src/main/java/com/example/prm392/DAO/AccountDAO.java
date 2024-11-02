package com.example.prm392.DAO;

import android.util.Log;
import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Account;
import com.example.prm392.model.AccountRole; // Ensure to import AccountRole
import com.example.prm392.model.AccountStatus; // Ensure to import AccountStatus

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    ConnectionClass connectionClass;

    public AccountDAO() {
        connectionClass = new ConnectionClass();
    }

    // Change password
    public boolean changePassword(String newPassword, String email) {
        boolean isPasswordChanged = false;
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "UPDATE `account` SET `password` = ? WHERE `email` = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, email);
                isPasswordChanged = preparedStatement.executeUpdate() > 0;
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isPasswordChanged;
    }

    // Check if an account exists
    public Account checkAccountExists(String email, String password) {
        Account account = null;
        try {
            Connection con = connectionClass.CONN();
            if (con != null) {
                String query = "SELECT * FROM `account` where `email` = ? and `password` = ?";
                PreparedStatement stm = con.prepareStatement(query);
                stm.setString(1, email);
                stm.setString(2, password);
                ResultSet rs = stm.executeQuery();
                if(rs.next()){
                    account = new Account(
                            rs.getInt("accID"),
                            rs.getString("fullname"),
                            rs.getBoolean("gender"),
                            rs.getString("email"),
                            rs.getString("password"), // Assuming you still need to fetch password
                            rs.getString("phone_number"),
                            new AccountRole(rs.getInt("roleID")), // Assuming roleID is stored as String
                            new AccountStatus(rs.getInt("status")), // Assuming status is stored as String
                            rs.getString("profile_picture"),
                            rs.getDate("create_at"),
                            rs.getDate("update_at")
                    );
                }
                rs.close();
                stm.close();
                con.close();
            } else {
                Log.e("ERROR", "Connection failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage());
        }
        return account;
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
            String query = "INSERT INTO `account` (`fullname`, `email`, `password`, `roleID`, `status`, `phone_number`, `profile_picture`) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, account.getFullname());
                preparedStatement.setString(2, account.getEmail());
                preparedStatement.setString(3, account.getPassword());
                preparedStatement.setInt(4, account.getRoleID().getRoleID());
                preparedStatement.setInt(5, account.getStatus().getStatusID());
                preparedStatement.setString(6, account.getPhoneNumber());
                preparedStatement.setString(7, account.getProfilePicture());

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

    // Get account by ID
    public Account getAccountById(int accountId) {
        Account account = null;
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "SELECT * FROM `account` WHERE `accID` = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, accountId);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    account = new Account(
                            rs.getInt("accID"),
                            rs.getString("fullname"),
                            rs.getBoolean("gender"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            new AccountRole(rs.getInt("roleID")), // Assuming roleID is stored as String
                            new AccountStatus(rs.getInt("status")), // Assuming status is stored as String
                            rs.getString("profile_picture"),
                            rs.getDate("create_at"),
                            rs.getDate("update_at")
                    );
                }
                rs.close();
                preparedStatement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection failed");
        }
        return account;
    }
}
