package com.example.prm392.activity.User;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.ConnectionClass;
import com.example.prm392.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private ConnectionClass connectionClass;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        loginButton = findViewById(R.id.btnLogin);
        emailEditText = findViewById(R.id.edtEmail);
        passwordEditText = findViewById(R.id.edtPassword);
        loginButton.setOnClickListener(view -> loginUser());

    }
    private void loginUser(){
        //convert login information to string
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //check if email or password is filled
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
        }
        //check email format
//        if(!isValidEmail(email)){
//            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
//        }

        //check user information in database
        //create a thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                //connect to database
                Connection connection = connectionClass.CONN();
                //check if database is connected
                if(connection!=null){
                    String query = "SELECT * FROM account where email = ? and password = ?;";
                    PreparedStatement stm = connection.prepareStatement(query);
                    stm.setString(1,email);
                    stm.setString(2,password);
                    ResultSet rs = stm.executeQuery();
                    if(rs.next()){
                        //this will be redirect to home page
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login successfull", Toast.LENGTH_LONG).show());
                    }else{
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show());;
                    }
                    rs.close();
                    stm.close();
                    connection.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}