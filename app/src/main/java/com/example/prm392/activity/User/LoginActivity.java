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
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        loginButton = findViewById(R.id.btnLogin);
        emailEditText = findViewById(R.id.edtEmail);
        passwordEditText = findViewById(R.id.edtPassword);
        connectionClass = new ConnectionClass();
        loginButton.setOnClickListener(this::loginUser);

    }
    private void loginUser(View view){
        //convert login information to string
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //check if email or password is filled
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Please fill enter email or password!", Toast.LENGTH_LONG).show();
        }

        //check email format
//        if(!isValidEmail(email)){
//            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
//        }

        //check user information in database
        //run in a thread
        new Thread(()->{
            try {
                //connect to database
                con = connectionClass.CONN();
                //check if database is connected
                if(con!=null){
                    String query = "SELECT * FROM account where email = ? and password = ?";
                    PreparedStatement stm = con.prepareStatement(query);
                    stm.setString(1,email);
                    stm.setString(2,password);
                    ResultSet rs = stm.executeQuery();
                    if(rs.next()){
                        // redirect to home activity

                    }else{
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show());;
                    }
                    rs.close();
                    stm.close();
                    con.close();
                }else{
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error connecting to database", Toast.LENGTH_LONG).show());
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void goToSignUp(View view) {
    }

    public void goToForgotPassword(View view) {
    }
}