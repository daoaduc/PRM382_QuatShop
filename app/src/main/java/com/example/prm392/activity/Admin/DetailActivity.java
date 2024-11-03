package com.example.prm392.activity.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {
    TextView detailDesc, detailTitle, detailPrice, detailStatus, detailQuantity;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton, returnButton;
    int currentStatusID;
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);
        detailDesc = findViewById(R.id.detailDesc);
        detailPrice = findViewById(R.id.detailPrice);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailStatus = findViewById(R.id.detailStatus);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        returnButton = findViewById(R.id.returnButton);
        detailQuantity = findViewById(R.id.detailQuantity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            long price = bundle.getLong("Price");
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedPrice = formatter.format(price) + " VND";
            int quantity = bundle.getInt("Quantity");
            // Hiển thị giá trị trong TextView
            detailPrice.setText(formattedPrice);
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailQuantity.setText("Số lượng còn lại: " + String.valueOf(quantity));
            detailStatus.setText(bundle.getString("Status"));
            currentStatusID = bundle.getInt("StatusID");
            Log.d("DetailActivity", "currentStatusID: " + currentStatusID);

            imageUrl = bundle.getString("Image");
            Glide.with(this).load(imageUrl).into(detailImage);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị hộp thoại xác nhận
                showDeleteConfirmationDialog(bundle);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ProductList.class);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("ProductID", bundle.getInt("ProductID"))
                        .putExtra("Title", bundle.getString("Title"))
                        .putExtra("Description", bundle.getString("Description"))
                        .putExtra("Price", bundle.getLong("Price"))
                        .putExtra("StatusID", bundle.getInt("StatusID"))
                        .putExtra("Quantity", bundle.getInt("Quantity"))
                        .putExtra("Image", imageUrl);
                startActivity(intent);
            }
        });
    }

    private void showDeleteConfirmationDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có thực sự muốn xóa sản phẩm?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int productId = bundle.getInt("ProductID");
                ProductDAO productDAO = new ProductDAO();
                Log.d("DetailActivity", "Product ID: " + productId);

                // Sử dụng ExecutorService để thực hiện tác vụ trong background thread
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    productDAO.deleteProduct(productId);  // Gọi hàm xóa trong ProductDAO

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference imageRef = storage.getReferenceFromUrl(imageUrl);

                        imageRef.delete().addOnSuccessListener(aVoid -> {
                            Log.d("DetailActivity", "File has been removed from Firebase Storage");
                        }).addOnFailureListener(e -> {
                            Log.e("DetailActivity", "Lỗi khi xóa file ảnh: " + e.getMessage());
                        });
                    }

                    runOnUiThread(() -> {
                        // Cập nhật UI trên main thread sau khi xóa sản phẩm
                        Toast.makeText(DetailActivity.this, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng hộp thoại nếu chọn "Không"
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại giao diện khi quay lại Activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            long price = bundle.getLong("Price");
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedPrice = formatter.format(price) + " VND";
            int quantity = bundle.getInt("Quantity");
            // Hiển thị giá trị trong TextView
            detailPrice.setText(formattedPrice);
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailQuantity.setText("Số lượng còn lại: " + String.valueOf(quantity));
            detailStatus.setText(bundle.getString("Status"));
            Log.d("ReturnActivity", "currentStatus: " + bundle.getString("Status"));
            currentStatusID = bundle.getInt("StatusID");

            imageUrl = bundle.getString("Image");
            Glide.with(this).load(imageUrl).into(detailImage);
        }
    }
}
