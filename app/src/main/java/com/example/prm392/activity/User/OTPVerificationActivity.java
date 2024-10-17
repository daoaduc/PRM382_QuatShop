package com.example.prm392.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.ConnectionClass;
import com.example.prm392.R;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OTPVerificationActivity extends AppCompatActivity {

    private EditText edtOTP;
    private ConnectionClass connectionClass;
    private String generatedOTP;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verify_register);

        edtOTP = findViewById(R.id.edtOTP);
        connectionClass = new ConnectionClass();

        generatedOTP = getIntent().getStringExtra("generatedOTP");
        userEmail = getIntent().getStringExtra("email");
    }

    public void verifyOTP(View view) {
        String enteredOTP = edtOTP.getText().toString().trim();
        if (enteredOTP.equals(generatedOTP)) {
            updateAccountStatus(userEmail);
        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAccountStatus(String email) {
        new Thread(() -> {
            try {
                Connection con = connectionClass.CONN();
                if (con != null) {
                    String query = "UPDATE `account` SET `status` = 1 WHERE `email` = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, email);
                    preparedStatement.executeUpdate();
                    con.close();
                    runOnUiThread(() -> {
                        Toast.makeText(OTPVerificationActivity.this, "Account verified!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(OTPVerificationActivity.this, LoginActivity.class);
//                        startActivity(intent);
                        finish();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void goToSignIn(View view) {
        Intent intent = new Intent(OTPVerificationActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
