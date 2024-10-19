package com.example.prm392.activity.Admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prm392.R;
import com.example.prm392.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton;
    EditText uploadProductName; // Thêm EditText cho mô tả và ngôn ngữ
    Uri uri;

    // Khai báo ActivityResultLauncher
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload);

        uploadImage = findViewById(R.id.uploadImage);
        saveButton = findViewById(R.id.saveButton);
        uploadProductName = findViewById(R.id.uploadProductName);

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
                            Log.d("UploadActivity", "No Image Selected"); // Ghi vào log
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
            String productName = uploadProductName.getText().toString().trim();

            if (productName.isEmpty()) {
                Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show();
                Log.d("UploadActivity", "Product name is empty"); // Ghi vào log
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

            // Tải hình ảnh lên Firebase Storage
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    Log.d("UploadActivity", "Image Uploaded"); // Ghi vào log

                    // Lấy URL hình ảnh sau khi tải lên thành công
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri urlImage = task.getResult();
                                Log.d("UploadActivity", "Image URL: " + urlImage.toString());
                                // Gọi phương thức để upload dữ liệu lên Realtime Database
                                uploadData(productName, urlImage.toString());
                            } else {
                                Toast.makeText(UploadActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                Log.d("UploadActivity", "Failed to get download URL"); // Ghi vào log
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("UploadActivity", "Upload Failed: " + e.getMessage()); // Ghi vào log
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            Log.d("UploadActivity", "No Image Selected"); // Ghi vào log
        }
    }

    private void uploadData(String productName, String imageURL) {
        // Khởi tạo Firebase Database với URL cụ thể
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://prm392-f39b8-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // Tạo một đối tượng DataClass để lưu thông tin sản phẩm
        Product dataClass = new Product(productName, imageURL);

        // Lấy thời gian hiện tại để sử dụng làm key trong Realtime Database
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        database.getReference("Sản phẩm").child(currentDate)
                .setValue(dataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UploadActivity.this, "Product Data Uploaded", Toast.LENGTH_SHORT).show();
                        finish(); // Kết thúc activity
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
