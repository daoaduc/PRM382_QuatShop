package com.example.prm392.activity.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.model.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountProfileActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etFullName, etPhoneNumber, etGender;
    private Spinner spinnerGender;
    private Button btnEditProfile, btnChangePassword;
    private boolean isEditMode = false;
    private Account account;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Retrieve the Account object from Intent
        account = (Account) getIntent().getSerializableExtra("account");

        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etFullName = findViewById(R.id.et_full_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        spinnerGender = findViewById(R.id.spinner_gender);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);


        setupGenderSpinner();
        // Load account data into the fields
        if (account != null) {
            loadAccountData(account);
        }
        String password = account.getPassword();
        String email = account.getEmail();
        // Set click listener for the "Edit Profile" button
        btnEditProfile.setOnClickListener(v -> toggleEditMode());

        // Set click listener for the "Change Password" button
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog(password, email));
    }
    private void setupGenderSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerGender.setAdapter(adapter);
    }

    private void loadAccountData(Account account) {
        // Populate the fields with data from the account

        etEmail.setText(account.getEmail());
        etFullName.setText(account.getFullname());
        etPhoneNumber.setText(account.getPhoneNumber());
        if (account.getGender() == 0) {
            spinnerGender.setSelection(0); // Male
        } else {
            spinnerGender.setSelection(1); // Female
        }
    }

    private void toggleEditMode() {
        if (isEditMode) {
            // Save Mode: Lock the fields and save the changes
            etUsername.setEnabled(false);
            etEmail.setEnabled(false);
            etFullName.setEnabled(false);
            etPhoneNumber.setEnabled(false);
            spinnerGender.setEnabled(false);

            btnEditProfile.setText("Edit Profile");

            // Save the updated data (you can extend this to save in the database)
            account.setEmail(etEmail.getText().toString());
            account.setFullname(etFullName.getText().toString());
            account.setPhoneNumber(etPhoneNumber.getText().toString());

            // Get the gender from the spinner (0 for Male, 1 for Female)
            int gender = spinnerGender.getSelectedItemPosition();
            account.setGender(gender);  // 0 for Male, 1 for Female

            // Here you can update the account in your database
            // Example: accountDAO.updateAccount(account);

            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();

        } else {
            // Edit Mode: Unlock the fields
            etUsername.setEnabled(true);
            etEmail.setEnabled(true);
            etFullName.setEnabled(true);
            etPhoneNumber.setEnabled(true);
            spinnerGender.setEnabled(true);

            btnEditProfile.setText("Save");
        }

        isEditMode = !isEditMode;
    }

    private void showChangePasswordDialog(String password, String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");

        // Add an EditText field for entering the new password
        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPassword = input.getText().toString();

                // Use executor service to handle the password change in the background
                executorService.submit(() -> {
                    AccountDAO accountDAO = new AccountDAO();
                    accountDAO.changePassword(newPassword, email);

                    // Toast must be shown on the UI thread
                    runOnUiThread(() -> {
                        Toast.makeText(AccountProfileActivity.this, "Password changed!", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

}
