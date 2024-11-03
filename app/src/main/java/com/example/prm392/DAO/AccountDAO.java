package com.example.prm392.DAO;

import android.util.Log;
import android.widget.Toast;

import com.example.prm392.ConnectionClass;
import com.example.prm392.activity.User.LoginActivity;
import com.example.prm392.model.Account;
import com.example.prm392.model.AccountRole;
import com.example.prm392.model.AccountStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    ConnectionClass connectionClass;

    public AccountDAO() {
        connectionClass = new ConnectionClass();
    }
    // Get id by email
    public int getIdByEmail(String email) {
        int id = -1;
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "SELECT `accid` FROM `account` WHERE `email` = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                resultSet.close();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
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
                isPasswordChanged = (preparedStatement.executeUpdate() > 0);
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
            //connect to database
            Connection con = connectionClass.CONN();
            //check if database is connected
            if(con!=null){
                String query = "SELECT * FROM `account` where `email` = ? and `password` = ?";
                PreparedStatement stm = con.prepareStatement(query);
                stm.setString(1,email);
                stm.setString(2,password);
                ResultSet rs = stm.executeQuery();
                if(rs.next()){
                    account = new Account(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),new AccountRole(rs.getInt(7)),new AccountStatus(rs.getInt(8)),rs.getString(9),rs.getDate(10),rs.getDate(11));
                }
                rs.close();
                stm.close();
                con.close();
            }else{
                Log.e("ERROR", "Connection failed");
            }
        }catch (SQLException e) {
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
            String query = "INSERT INTO `account` (`fullname`, `email`, `password`, `roleID`, `status`) VALUES (?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, account.getFullname());
                preparedStatement.setString(2, account.getEmail());
                preparedStatement.setString(3, account.getPassword());
                preparedStatement.setInt(4, account.getRole().getRoleID());
                preparedStatement.setInt(5, account.getStatus().getStatusID());

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

    public String getUserName(int userId) {
        return "";
    }

    public Account getAccountById(int userId) {
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "SELECT * FROM `account` WHERE `accid` = ?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new Account(resultSet.getInt(1), resultSet.getString(2));
                }
                resultSet.close();
                preparedStatement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addGoogleAccount(String email, String displayName, String password) {
        Connection con = connectionClass.CONN();
        if (con != null) {
            String query = "INSERT INTO `account` (`email`, `fullname`, `password`, `roleID`, `status`) VALUES (?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, displayName);
                preparedStatement.setString(3, password);
                preparedStatement.setInt(4, 2);
                preparedStatement.setInt(5, 1);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
