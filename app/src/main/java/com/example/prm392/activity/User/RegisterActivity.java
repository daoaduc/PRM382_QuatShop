package com.example.prm392.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.ConnectionClass;
import com.example.prm392.R;
import com.example.prm392.utils.SendMail;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullname, edtEmail, edtPassword, edtConfirmPassword;
    private ConnectionClass connectionClass;
    private String generatedOTP;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        connectionClass = new ConnectionClass();

        edtFullname = findViewById(R.id.edtFullname);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
    }

    public void registerUser(View view) {
        String fullname = edtFullname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 4 || password.length() > 16) {
            Toast.makeText(this, "Password must be 4 to 16 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            if (emailExists(email)) {
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show());
            } else {
                generatedOTP = SendMail.generateOTP();
                boolean emailSent = SendMail.sendMail(email, generatedOTP);

                if (emailSent) {
                    String hashedPassword = hashPassword(password);
                    saveUserToDatabase(fullname, email, hashedPassword);

                    Intent intent = new Intent(RegisterActivity.this, OTPVerificationActivity.class);
                    intent.putExtra("generatedOTP", generatedOTP);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show());
                }
            }
        }).start();
    }

    // Android's built-in Patterns class
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean emailExists(String email) {
        boolean exists = false;

        try {
            Connection con = connectionClass.CONN();
            if (con != null) {
                String query = "SELECT * FROM `account` WHERE `email` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);

                ResultSet resultSet = preparedStatement.executeQuery();
                exists = resultSet.next();
                resultSet.close();
                preparedStatement.close();
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    private void saveUserToDatabase(String fullname, String email, String password) {
        new Thread(() -> {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Connection failed", Toast.LENGTH_SHORT).show());
                } else {
                    String query = "INSERT INTO `account` (`fullname`, `email`, `password`, `roleID`, `status`) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, fullname);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);
                    preparedStatement.setInt(4, 2); // customer
                    preparedStatement.setInt(5, 2); // Unverified

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // hash the password using MD5
    private String hashPassword(String password) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array into a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

}
