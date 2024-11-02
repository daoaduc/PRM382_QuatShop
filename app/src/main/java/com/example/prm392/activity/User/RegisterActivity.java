package com.example.prm392.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.R;
import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.model.Account;
import com.example.prm392.model.AccountRole;
import com.example.prm392.model.AccountStatus;
import com.example.prm392.utils.SendMail;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullname, edtEmail, edtPassword, edtConfirmPassword;
    private AccountDAO accountDAO;
    private String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        accountDAO = new AccountDAO();

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
            if (accountDAO.emailExists(email)) {
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show());
            } else {
                generatedOTP = SendMail.generateOTP();
                boolean emailSent = SendMail.sendMail(email, generatedOTP);

                if (emailSent) {
                    String hashedPassword = hashPassword(password);
                    Account newAccount = new Account();
                    newAccount.setFullname(fullname);
                    newAccount.setEmail(email);
                    newAccount.setPassword(hashedPassword);
                    newAccount.setRoleID(new AccountRole(2)); // customer
                    newAccount.setStatus(new AccountStatus(2)); // Unverified
                    accountDAO.saveUserToDatabase(newAccount);

                    Intent intent = new Intent(this, OTPVerificationActivity.class);
                    intent.putExtra("generatedOTP", generatedOTP);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show());
                }
            }
        }).start();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    public void goToSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
