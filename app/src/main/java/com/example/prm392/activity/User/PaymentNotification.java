package com.example.prm392.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.R;

public class PaymentNotification extends AppCompatActivity {

    TextView textViewNotify, textViewMessage;
    Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_notification);

        textViewNotify = findViewById(R.id.textViewNotify);
        textViewMessage = findViewById(R.id.textViewMessage);
        Intent intent = getIntent();
        textViewNotify.setText(intent.getStringExtra("textViewNotify"));
        textViewMessage.setText(intent.getStringExtra("textViewMessage"));
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            Intent intent2 = new Intent(PaymentNotification.this, MainActivity2.class);
            startActivity(intent2);
            finish();
        });
    }
}