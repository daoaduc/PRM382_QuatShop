package com.example.prm392.activity.Admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prm392.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton;
    EditText uploadProductName;
    Uri uri;

    // Khai báo ActivityResultLauncher
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload);

        uploadImage = findViewById(R.id.uploadImage);
        saveButton = findViewById(R.id.saveButton);
        uploadProductName = findViewById(R.id.uploadProductName); // Khai báo EditText

        // Khởi tạo ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                    uri = data.getData();
                                    uploadImage.setImageURI(uri);
                            }
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDownloads();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToFirebase();
            }
        });
    }

    private void openDownloads() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Chọn hình ảnh
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void uploadImageToFirebase() {
        if (uri != null) {
            String productName = uploadProductName.getText().toString().trim(); // Lấy tên sản phẩm từ EditText
            if (productName.isEmpty()) {
                Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show();
                return;
            }
            String fileName = productName + ".jpg"; // Sử dụng tên sản phẩm làm tên tệp
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Sản phẩm")
                    .child(fileName); // Sử dụng tên tệp

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();


            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss(); // Đóng dialog khi thành công
                    Toast.makeText(UploadActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss(); // Đóng dialog khi thất bại
                    Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
}
