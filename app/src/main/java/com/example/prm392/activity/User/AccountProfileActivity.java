package com.example.prm392.activity.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.model.Account;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountProfileActivity extends AppCompatActivity {

    private EditText etEmail, etFullName, etPhoneNumber, etGender;
    private Spinner spinnerGender;
    private Button btnEditProfile, btnChangePassword, btnUploadIMG;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgProfilePicture;
    AccountDAO accountDAO;
    private boolean isEditMode = false;
    private Account account;
    private ImageButton btnBack;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Retrieve the Account object from Intent
        Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra("account");

        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etFullName = findViewById(R.id.et_full_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        spinnerGender = findViewById(R.id.spinner_gender);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnUploadIMG = findViewById(R.id.btn_upload_image);
        btnBack = findViewById(R.id.btn_back);
        imgProfilePicture = findViewById(R.id.img_profile_picture);


        setupGenderSpinner();
        // Load account data into the fields
        if (account != null) {
            loadAccountData(account);
        }
        String password = account.getPassword();
        String email = account.getEmail();
        // Set click listeners
        btnBack.setOnClickListener(v -> onBackPressed());
        btnEditProfile.setOnClickListener(v -> toggleEditMode());
        btnUploadIMG.setOnClickListener(v -> openImagePicker());
        // Set click listener for the "Change Password" button
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog(password, email));
    }
    @Override
    protected void onStart() {
        super.onStart();

        int accID = getIntent().getIntExtra("accID", -1);
        if (accID != -1) {
            account = fetchAccountData(accID);  // Method to load account from database
        }
    }
    private Account fetchAccountData(int accID) {
        executorService.submit(() -> {
            accountDAO = new AccountDAO();
            account = accountDAO.getAccountByID(accID);

            // Load data into views on the UI thread
            runOnUiThread(() -> {
                if (account != null) {
                    loadAccountData(account);
                }
            });
        });
        return account;
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
        if (account.getProfilePicture() != null) {
            Uri savedImageUri = Uri.parse(account.getProfilePicture());
            imgProfilePicture.setImageURI(savedImageUri);
        }else{
            Toast.makeText(this, "No Profile Picture", Toast.LENGTH_SHORT).show();
        }
        etEmail.setText(account.getEmail());
        etFullName.setText(account.getFullname());
        etPhoneNumber.setText(account.getPhoneNumber());
        if (account.isGender()) {
            spinnerGender.setSelection(0); // Male
        } else {
            spinnerGender.setSelection(1); // Female
        }
    }

    private void toggleEditMode() {
        if (isEditMode) {
            // Save Mode: Lock the fields and save the changes
            etFullName.setEnabled(false);
            etPhoneNumber.setEnabled(false);
            spinnerGender.setEnabled(false);

            btnEditProfile.setText("Edit Profile");
            String fullName = etFullName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();

            // Get the gender from the spinner (true for Male, false for Female)
            boolean gender = spinnerGender.getSelectedItemPosition() == 0;

            executorService.submit(() -> {
                accountDAO = new AccountDAO();
                boolean updated = accountDAO.updateAccount(fullName, phoneNumber, gender, account.getAccID());

                runOnUiThread(() -> {
                    if (!updated) {
                        Toast.makeText(AccountProfileActivity.this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AccountProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            // Update account data locally
            account.setFullname(fullName);
            account.setPhoneNumber(phoneNumber);
            account.setGender(gender);

        } else {
            // Edit Mode: Unlock the fields
            etFullName.setEnabled(true);
            etPhoneNumber.setEnabled(true);
            spinnerGender.setEnabled(true);

            btnEditProfile.setText("Save");
        }

        isEditMode = !isEditMode;
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void showChangePasswordDialog(String currentPassword, String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");

        // Create a layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        // Initialize the input fields
        final EditText etCurrentPassword = dialogView.findViewById(R.id.et_current_password);
        final EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        final EditText etConfirmPassword = dialogView.findViewById(R.id.et_confirm_password);
        //final TextView tvErrorMessage = dialogView.findViewById(R.id.tv_error_message); // TextView for error messages

        // Set up the buttons
        builder.setPositiveButton("OK", null); // Set null for now to handle it later
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());


        AlertDialog dialog = builder.create();

        // Override the onShow method to handle button clicks after dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String enteredCurrentPassword = etCurrentPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                String enteredCurrentPasswordHash = hashPassword(enteredCurrentPassword);

                if (!enteredCurrentPasswordHash.equals(currentPassword)) {
                    Toast.makeText(AccountProfileActivity.this, "Current password is incorrect.", Toast.LENGTH_SHORT).show();
                } else if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(AccountProfileActivity.this, "Please enter new password and confirm password.", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(AccountProfileActivity.this, "New password and confirm password do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    executorService.submit(() -> {
                        accountDAO = new AccountDAO();
                        accountDAO.changePassword(hashPassword(newPassword), email);
                        runOnUiThread(() -> {
                            Toast.makeText(AccountProfileActivity.this, "Password changed!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    });
                }
            });
        });

        // Show the dialog
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Convert image to base64 string and save to database
            saveImageToDatabase(imageUri);
        }
    }

    private void saveImageToDatabase(Uri imageUri) {
        // Convert image to base64 string
        String base64Image = convertImageToBase64(imageUri);
        if (base64Image == null) {
            Toast.makeText(this, "Failed to convert image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save image data to database in a separate thread
        executorService.submit(() -> {
            accountDAO = new AccountDAO();
            boolean success = accountDAO.uploadProfileIMG(base64Image, account.getAccID()); // Use your upload method

            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(AccountProfileActivity.this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
                    imgProfilePicture.setImageURI(imageUri); // Optionally update the ImageView
                } else {
                    Toast.makeText(AccountProfileActivity.this, "Failed to save image!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String convertImageToBase64(Uri imageUri) {
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri);
             ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream()) {

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            byte[] imageBytes = byteBuffer.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
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

}
