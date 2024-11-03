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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prm392.DAO.CategoryDAO;
import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton, cancelButton;
    EditText uploadProductName, uploadDesc, uploadPrice, uploadQuantity;
    Spinner uploadCategory;
    Uri uri;

    // Khai báo ActivityResultLauncher và ExecutorService
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload);

        uploadImage = findViewById(R.id.uploadImage);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        uploadProductName = findViewById(R.id.uploadProductName);
        uploadDesc = findViewById(R.id.uploadDesc);
        uploadPrice = findViewById(R.id.uploadPrice);
        uploadQuantity = findViewById(R.id.uploadQuantity);


        uploadCategory = findViewById(R.id.uploadCategory);

        // Khởi tạo ExecutorService
        executorService = Executors.newSingleThreadExecutor();

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
                            Log.d("UploadActivity", "No Image Selected");
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

        // Tải danh mục và thiết lập adapter trong background thread
        executorService.execute(() -> {
            loadCategories(uploadCategory);
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToFirebase();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadCategories(Spinner uploadCategory) {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<ProductCategory> categories = categoryDAO.getAllCategories();

        // Danh sách chứa đối tượng CategoryItem
        List<ProductCategory> categoryItems = new ArrayList<>();
        for (ProductCategory category : categories) {
            categoryItems.add(new ProductCategory(category.getCategoryID(), category.getCategoryName()));
        }

        // Cập nhật UI từ background thread
        runOnUiThread(() -> {
            ArrayAdapter<ProductCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            uploadCategory.setAdapter(adapter);
        });
    }


    private void openDownloads() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void uploadImageToFirebase() {
        if (uri != null) {
            String productName = uploadProductName.getText().toString().trim();
            String productDesc = uploadDesc.getText().toString().trim();

            String priceString = uploadPrice.getText().toString().trim();
            long price = 0;
            if (!priceString.isEmpty()) {
                price = Long.parseLong(priceString);
            }

            String quantityString = uploadQuantity.getText().toString().trim();
            int quantity = 0;
            if (!quantityString.isEmpty()) {
                quantity = Integer.parseInt(quantityString);
            }

            if (quantity > 999) {
                Toast.makeText(this, "Số lượng tối đa là 999", Toast.LENGTH_SHORT).show();
                return;
            }
            // Lay thong tin danh mục
            ProductCategory selectedCategory = (ProductCategory) uploadCategory.getSelectedItem();

            // Kiểm tra các trường thông tin đầu vào
            if (productName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            if (productDesc.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mô tả sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            if (priceString.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantityString.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số lượng sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            final long finalPrice = price;
            final int finalQuantity = quantity;
            // Tạo ExecutorService để tải sản phẩm lên
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(() -> {
                ProductDAO productDAO = new ProductDAO();
                int productID = productDAO.getLastProductID() + 1; // Lấy productID + 1
                String fileName = productID + ".jpg"; // Đặt tên ảnh bằng productID + 1

                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("Sản phẩm")
                        .child(fileName);

                runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
                    builder.setCancelable(false);
                    builder.setView(R.layout.progress_layout);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Tải hình ảnh lên Firebase
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(UploadActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            Log.d("UploadActivity", "Image Uploaded");

                            // Lấy URL hình ảnh sau khi tải lên thành công
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri urlImage = task.getResult();
                                        Log.d("UploadActivity", "Image URL: " + urlImage.toString());

                                        ExecutorService executorService = Executors.newSingleThreadExecutor();

                                        // Thực thi task trong background thread
                                        executorService.execute(() -> {
                                            // Cập nhật URL hình ảnh vào đối tượng Product
                                            Product p = new Product(productName, productDesc, finalPrice, finalQuantity, selectedCategory, urlImage.toString());
                                            // Thêm sản phẩm vào cơ sở dữ liệu
                                            ProductDAO pdao = new ProductDAO();
                                            pdao.insertProduct(p);

                                            runOnUiThread(() -> Toast.makeText(UploadActivity.this, "Product uploaded successfully", Toast.LENGTH_SHORT).show());
                                            finish();
                                        });
                                    } else {
                                        Toast.makeText(UploadActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                        Log.d("UploadActivity", "Failed to get download URL");
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("UploadActivity", "Upload Failed: " + e.getMessage());
                        }
                    });
                });
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            Log.d("UploadActivity", "No Image Selected");
        }
    }

}

