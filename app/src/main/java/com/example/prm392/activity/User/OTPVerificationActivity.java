//package com.example.prm392.activity.User;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.prm392.DAO.AccountDAO;
//import com.example.prm392.R;
//
//public class OTPVerificationActivity extends AppCompatActivity {
//
//    private EditText edtOTP;
//    private AccountDAO accountDAO;
//    private String generatedOTP;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_verify_register);
//
//        edtOTP = findViewById(R.id.edtOTP);
//        accountDAO = new AccountDAO();
//
//        generatedOTP = getIntent().getStringExtra("generatedOTP");
//        userEmail = getIntent().getStringExtra("email");
//    }
//
//    public void verifyOTP(View view) {
//        String enteredOTP = edtOTP.getText().toString().trim();
//        if (enteredOTP.equals(generatedOTP)) {
//            new Thread(() -> {
//                accountDAO.updateAccountStatus(userEmail);
//                runOnUiThread(() -> {
//                    Toast.makeText(OTPVerificationActivity.this, "Account verified!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(OTPVerificationActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                });
//            }).start();
//        } else {
//            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void goToSignIn(View view) {
//        Intent intent = new Intent(OTPVerificationActivity.this,LoginActivity.class);
//        startActivity(intent);
//    }
//}