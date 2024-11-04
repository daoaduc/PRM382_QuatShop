package com.example.prm392.activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText edtNewPassword, edtConfirmPassword;
    TextView tvMessage, tvTimer;
    AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        tvMessage = findViewById(R.id.tvMessage);
        tvTimer = findViewById(R.id.tvTimer);
    }

    public void goToSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ResourceAsColor")
    public void changePassword(View view) {
        accountDAO = new AccountDAO();
        // Check input empty
        if(edtNewPassword.getText().toString().isEmpty() || edtConfirmPassword.getText().toString().isEmpty()){
            tvMessage.setTextColor(R.color.red);
            tvMessage.setText("Please fill in all fields");
            tvMessage.setTextColor(tvMessage.getTextColors().getDefaultColor());
            return;
        }

        // Check input password
        if (edtNewPassword.length() < 4 || edtNewPassword.length() > 16) {
            Toast.makeText(this, "Password must be 4 to 16 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check input password fields
        if(!edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
            tvMessage.setTextColor(R.color.red);
            tvMessage.setText("Confirm password does not match");
            tvMessage.setTextColor(tvMessage.getTextColors().getDefaultColor());
            return;
        }

        // Update password in database
        new Thread(() -> {
            String newPassword = edtNewPassword.getText().toString();
            newPassword = hashPassword(newPassword);
            String email = getIntent().getStringExtra("email");
            boolean isPasswordChanged = accountDAO.changePassword(newPassword, email);
            if(isPasswordChanged){
                runOnUiThread(()->{
                    tvMessage.setText("Password changed successfully");
                });
            }else{
                runOnUiThread(()->{
                    tvMessage.setTextColor(R.color.red);
                    tvMessage.setText("Failed to change password");
                    tvMessage.setTextColor(tvMessage.getTextColors().getDefaultColor());
                });
            }

        }).start();
    }

    private String hashPassword(String password) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

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