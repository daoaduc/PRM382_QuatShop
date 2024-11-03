//package com.example.prm392.activity.User;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.credentials.GetCredentialRequest;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.prm392.DAO.AccountDAO;
//import com.example.prm392.R;
//import com.example.prm392.model.Account;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText emailEditText, passwordEditText;
//
//    AccountDAO accountDAO;
//    CheckBox saveLoginCheckBox;
//
//    private SignInButton googleSignInButton;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_login);
//        emailEditText = findViewById(R.id.edtEmail);
//        passwordEditText = findViewById(R.id.edtPassword);
//        accountDAO = new AccountDAO();
//        saveLoginCheckBox = findViewById(R.id.chkSaveLogin);
//        googleSignInButton = findViewById(R.id.gg_sign_in_button);
//
//        getLoginInfo();
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        if(mGoogleSignInClient != null) {
//            mGoogleSignInClient.signOut();
//        }
//
//        googleSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, 1);
//            }
//        });
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }
//
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            if (account != null) {
//                Log.d("GoogleSignIn", "Signed in successfully");
//                Log.d("GoogleSignIn", "Display Name: " + account.getDisplayName());
//                Log.d("GoogleSignIn", "Email: " + account.getEmail());
//                new Thread(() -> {
//                    AccountDAO accountDAO = new AccountDAO();
//                    if(accountDAO.emailExists(account.getEmail())){
//                        saveUserID(accountDAO.getIdByEmail(account.getEmail()));
//                    }else{
//                        accountDAO.addGoogleAccount(account.getEmail(), account.getDisplayName(), hashPassword(randomPassword()));
//                        saveUserID(accountDAO.getIdByEmail(account.getEmail()));
//                    }
//                    runOnUiThread(() -> {
//                        Intent intent = new Intent(this, MainActivity2.class);
//                        startActivity(intent);
//                        finish();
//                    });
//                }).start();
//            }
//        } catch (ApiException e) {
//            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
//        }
//    }
//
//    private String randomPassword() {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        SecureRandom random = new SecureRandom();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 8; i++) {
//            int index = random.nextInt(characters.length());
//            sb.append(characters.charAt(index));
//        }
//        return sb.toString();
//    }
//
//    private boolean isValidEmail(String email) {
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    public void goToSignUp(View view) {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
//    }
//
//    public void goToForgotPassword(View view) {
//        Intent intent = new Intent(this, ForgotPasswordActivity.class);
//        startActivity(intent);
//    }
//
//    private String hashPassword(String password) {
//        String hashedPassword = null;
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
//
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hashedBytes) {
//                sb.append(String.format("%02x", b));
//            }
//            hashedPassword = sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return hashedPassword;
//    }
//
//    public void loginUser(View view) {
//        //convert login information to string
//        String email = emailEditText.getText().toString();
//        String password = passwordEditText.getText().toString();
//
//
//        //check if email or password is filled
//        if(email.isEmpty()||password.isEmpty()){
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        //check email format
//        if(!isValidEmail(email)){
//            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        //check user information matches database
//        new Thread(() -> {
//            String hashedPassword = hashPassword(password);
//            Account account = accountDAO.checkAccountExists(email, hashedPassword);
//            if (account != null) {
//                // save login information
//                if (saveLoginCheckBox.isChecked()) {
//                    saveLogin(email, password);
//                }
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();
//                    // save user id for later use
//                    saveUserID(account.getAccID());
//                    // intent to main activity
//                    Intent intent = new Intent(this, MainActivity2.class);
//                    startActivity(intent);
//                    // finish current activity
//                    finish();
//                });
//            }else{
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
//                });
//            }
//        }).start();
//    }
//
//    // Save user id function
//    private void saveUserID(int userID) {
//        SharedPreferences sharedPref = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("userID", userID);
//        editor.apply();
//    }
//
//    // Save login information function
//    private void saveLogin(String username, String password) {
//        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//
//        editor.putString("username", username);
//        editor.putString("password", password);
//        editor.apply();
//    }
//
//    // Get login information function
//    private void getLoginInfo() {
//        // delete save login information
//        SharedPreferences saveLoginPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        String username = saveLoginPref.getString("username", "");
//        String password = saveLoginPref.getString("password", "");
//
//        if (!username.isEmpty() && !password.isEmpty()) {
//            emailEditText.setText(username);
//            passwordEditText.setText(password);
//            saveLoginCheckBox.setChecked(true);
//            loginUser(null);
//        }
//    }
//
//    // Clear login information function
//    private void clearLoginInfo() {
//        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        if(editor != null){
//            editor.clear();
//            editor.apply();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//    public void signInWithGoogle(View view) {
//
//    }
//
//    public String generateNonce() {
//        // Generate a random byte array
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] nonce = new byte[16]; // 16 bytes = 128 bits
//        secureRandom.nextBytes(nonce);
//
//        // Encode the byte array to a Base64 string
//        return Base64.encodeToString(nonce, Base64.NO_WRAP);
//    }
//}