package com.example.prm392.activity.User;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.R;

public class AccountProfileActivity extends AppCompatActivity {
    private EditText etUsername, etEmail;
    private Button btnEditProfile;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        btnEditProfile = findViewById(R.id.btn_edit_profile);

        // Set click listener for the "Edit Profile" button
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditMode();
            }
        });
    }

    private void toggleEditMode() {
        if (isEditMode) {
            // Save Mode: Lock the fields and save the changes
            etUsername.setEnabled(false);
            etEmail.setEnabled(false);
            etUsername.setFocusable(false);
            etUsername.setFocusableInTouchMode(false);
            etEmail.setFocusable(false);
            etEmail.setFocusableInTouchMode(false);

            btnEditProfile.setText("Edit Profile");

            // Save the data (This part can be extended to save to a database or shared preferences)
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            // Save the username and email to backend or local storage

        } else {
            // Edit Mode: Unlock the fields
            etUsername.setEnabled(true);
            etEmail.setEnabled(true);
            etUsername.setFocusableInTouchMode(true);
            etEmail.setFocusableInTouchMode(true);

            btnEditProfile.setText("Save");
        }

        isEditMode = !isEditMode;
    }
}
