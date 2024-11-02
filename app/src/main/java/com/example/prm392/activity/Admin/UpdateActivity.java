package com.example.prm392.activity.Admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.DAO.ProductStatusDAO;
import com.example.prm392.R;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductCategory;
import com.example.prm392.model.ProductStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton, cancelButton;
    EditText updateDesc, updateProductName, updatePrice, updateQuantity;
    Spinner updateStatus;
    Uri uri;
    String oldImageURL;
    int productID, currentStatusID;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        updateImage = findViewById(R.id.updateImage);
        updateButton = findViewById(R.id.updateButton);
        cancelButton = findViewById(R.id.cancelButton);
        updateDesc = findViewById(R.id.updateDesc);
        updateProductName = findViewById(R.id.updateProductName);
        updatePrice = findViewById(R.id.updatePrice);
        updateQuantity = findViewById(R.id.updateQuantity);
        updateStatus = findViewById(R.id.updateStatus);

        // Nhận dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(UpdateActivity.this).load(bundle.getString("Image")).into(updateImage);
            updateProductName.setText(bundle.getString("Title"));
            updateDesc.setText(bundle.getString("Description"));
            updatePrice.setText(String.valueOf(bundle.getLong("Price")));
            updateQuantity.setText(String.valueOf(bundle.getInt("Quantity")));
            oldImageURL = bundle.getString("Image");
            productID = bundle.getInt("ProductID");
            Log.d("UpdateActivity23123", "currentProductID3213: " + productID);
            currentStatusID = bundle.getInt("StatusID");
            Log.d("UpdateActivity2312", "currentStatusID3213: " + currentStatusID);
        }

        // Khởi tạo ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Load danh mục vào Spinner (tương tự như trong UploadActivity)
        executorService.execute(() -> {
            loadStatus(updateStatus);
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                uri = data.getData();
                                updateImage.setImageURI(uri);
                            }
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDownloads(activityResultLauncher);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadStatus(Spinner updateStatus) {
        ProductStatusDAO productStatusDAO = new ProductStatusDAO();
        List<ProductStatus> status = productStatusDAO.getAllProductStatus();

        // Danh sách chứa đối tượng CategoryItem
        List<ProductStatus> productStatus = new ArrayList<>();
        for (ProductStatus status1 : status) {
            productStatus.add(new ProductStatus(status1.getStatusID(), status1.getStatusName()));
        }

        // Cập nhật UI từ background thread
        runOnUiThread(() -> {
            ArrayAdapter<ProductStatus> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productStatus);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            updateStatus.setAdapter(adapter);

            for (int i = 0; i < productStatus.size(); i++) {
                if (productStatus.get(i).getStatusID() == currentStatusID) {
                    updateStatus.setSelection(i);
                    break;
                }
            }
        });
    }

    private void openDownloads(ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void updateProduct() {
        String productName = updateProductName.getText().toString().trim();
        String productDesc = updateDesc.getText().toString().trim();
        long price = Long.parseLong(updatePrice.getText().toString().trim());
        int quantity = Integer.parseInt(updateQuantity.getText().toString().trim());
        ProductStatus selectedStatus = (ProductStatus) updateStatus.getSelectedItem();

        if (quantity > 999) {
            Toast.makeText(this, "Số lượng tối đa là 999", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productName.isEmpty() || productDesc.isEmpty() || price <= 0 || quantity <= 0) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        if (uri != null) {
            // Cập nhật hình ảnh mới
            String fileName = productID + ".jpg";
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Sản phẩm")
                    .child(fileName);

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String newImageUrl = task.getResult().toString();
                                ExecutorService executorService = Executors.newSingleThreadExecutor();

                                // Thực thi task trong background thread
                                executorService.execute(() -> {
                                    saveProduct(productName, productDesc, price, quantity, selectedStatus, newImageUrl);
                                });

                            } else {
                                Toast.makeText(UpdateActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            // Không thay đổi hình ảnh, chỉ cập nhật thông tin khác
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            // Thực thi task trong background thread
            executorService.execute(() -> {
                saveProduct(productName, productDesc, price, quantity, selectedStatus, oldImageURL);
            });
        }
    }

    private void saveProduct(String productName, String productDesc, long price, int quantity, ProductStatus status, String imageUrl) {
        ProductDAO productDAO = new ProductDAO();
        Product updatedProduct = new Product(productID, productName, price, quantity, productDesc, status, imageUrl);
        productDAO.updateProduct(updatedProduct);

        runOnUiThread(() -> {
            Toast.makeText(UpdateActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();

            // Trở về DetailActivity với dữ liệu mới
            Intent intent = new Intent(UpdateActivity.this, DetailActivity.class);
            intent.putExtra("ProductID", productID);
            intent.putExtra("Title", productName);
            intent.putExtra("Description", productDesc);
            intent.putExtra("Price", price);
            intent.putExtra("Status", status.getStatusName());
            intent.putExtra("StatusID", status.getStatusID());
            intent.putExtra("Quantity", quantity);
            intent.putExtra("Image", imageUrl);
            startActivity(intent);
            finish();
        });
    }

}
