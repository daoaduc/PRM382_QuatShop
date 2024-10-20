package com.example.prm392.activity.User;

import static com.example.prm392.utils.SendMail.generateOTP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.model.Account;
import com.example.prm392.utils.SendMail;

public class ForgotPasswordActivity extends AppCompatActivity {
    View emailLayout, otpLayout;
    EditText edtEmail;
    TextView btnSendOTP;
    TextView tvMessage;
    Button btnVerifyOTP;
    EditText edtOTP;
    TextView tvTimer;
    String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailLayout = findViewById(R.id.emailLayout);
        otpLayout = findViewById(R.id.otpLayout);

        edtEmail = findViewById(R.id.edtEmail);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        tvMessage = findViewById(R.id.tvMessage);
        tvTimer = findViewById(R.id.tvTimer);

        // Validate email
        btnSendOTP.setOnClickListener(v -> {
            tvMessage.setTextColor(tvMessage.getTextColors().getDefaultColor());
            executeEmailValidation(edtEmail.getText().toString());
        });


        edtOTP = findViewById(R.id.edtOTP);
        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);

        // Verify OTP
        btnVerifyOTP.setOnClickListener(v -> {
            executeOTPValidation();
        });
    }


    @SuppressLint("ResourceAsColor")
    private void executeOTPValidation(){
        int otp = Integer.parseInt(edtOTP.getText().toString());
        //check if OTP is valid
        if(generatedOTP.equals("expired")){
            tvMessage.setTextColor(R.color.red);
            tvMessage.setText("OTP is expired!!");
        }else{
            if(otp == Integer.parseInt(generatedOTP)){
                // Go to reset password activity
                Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                intent.putExtra("email", edtEmail.getText().toString());
                startActivity(intent);
            }else{
                //Show error message
                tvMessage.setTextColor(R.color.red);
                tvMessage.setText("Incorrect OTP!!");
            }
        }

    }

    private void startOTPTimer(long duration){
        //start timer
        CountDownTimer countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished/1000));
            }
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                tvTimer.setText("0");
                tvTimer.setVisibility(View.INVISIBLE);
                tvMessage.setTextColor(R.color.red);
                tvMessage.setText("OTP expired, please try again");
                generatedOTP = "expired";
            }
        };
        countDownTimer.start();
    }

    @SuppressLint("ResourceAsColor")
    private void executeEmailValidation(String email){
        new Thread (() -> {
            //check if email exists in database
            AccountDAO accountDAO = new AccountDAO();
            Boolean isEmailExists = accountDAO.emailExists(email);
            if(isEmailExists){
                SendMail sendMail = new SendMail();
                // Generate OTP
                generatedOTP = sendMail.generateOTP();
                // Send OTP to email
                boolean isMailSent = SendMail.sendMail(edtEmail.getText().toString(), generatedOTP);
                if(isMailSent){
                    runOnUiThread(() -> {
                        //Show success message
                        tvMessage.setText("OTP sent successfully");
                        //Show OTP layout
                        otpLayout.setVisibility(View.VISIBLE);
                        //Display timer
                        tvTimer.setVisibility(View.VISIBLE);
                        //Start timer
                        startOTPTimer(60000);
                    });
                }else{
                    //Show error message
                    runOnUiThread(() -> {
                        tvMessage.setTextColor(R.color.red);
                        tvMessage.setText("Failed to send OTP");
                    });
                }
            }else{
                //Show error message
                runOnUiThread(()-> {
                    tvMessage.setTextColor(R.color.red);
                    tvMessage.setText("Email does not exist");
                });
            }
        }).start();
    }


    public void goToSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}