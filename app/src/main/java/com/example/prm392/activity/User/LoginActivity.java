package com.example.prm392.activity.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.ConnectionClass;
import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.model.Account;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    AccountDAO accountDAO;
    CheckBox saveLoginCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        loginButton = findViewById(R.id.btnLogin);
        emailEditText = findViewById(R.id.edtEmail);
        passwordEditText = findViewById(R.id.edtPassword);
        accountDAO = new AccountDAO();
        saveLoginCheckBox = findViewById(R.id.chkSaveLogin);
        getLoginInfo();

    }



    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToForgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
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

    public void loginUser(View view) {
        //convert login information to string
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        //check if email or password is filled
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Please fill enter email or password!", Toast.LENGTH_LONG).show();
            return;
        }

        //check email format
        if(!isValidEmail(email)){
            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
            return;
        }

        //check user information matches database
        new Thread(() -> {
            String hashedPassword = hashPassword(password);
            Account account = accountDAO.checkAccountExists(email, hashedPassword);
            if (account != null) {
                // save login information
                if (saveLoginCheckBox.isChecked()) {
                    saveLogin(email, password);
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();
                    // save user id for later use
                    saveUserID(account.getAccID());
                    // intent to main activity
                    Intent intent = new Intent(this, MainActivity2.class);
                    startActivity(intent);
                });
            }else{
                runOnUiThread(() -> {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    // Save user id function
    private void saveUserID(int userID) {
        SharedPreferences sharedPref = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("userID", userID);
        editor.apply();
    }

    // Save login information function
    private void saveLogin(String username, String password) {
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    // Get login information function
    private void getLoginInfo() {
        // delete save login information
        SharedPreferences saveLoginPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = saveLoginPref.getString("username", "");
        String password = saveLoginPref.getString("password", "");

        if (!username.isEmpty() && !password.isEmpty()) {
            emailEditText.setText(username);
            passwordEditText.setText(password);
            saveLoginCheckBox.setChecked(true);
            loginUser(null);
        }
    }

    // Clear login information function
    private void clearLoginInfo() {
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(editor != null){
            editor.clear();
            editor.apply();
        }
    }
}