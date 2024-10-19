package com.example.prm392.activity.Admin;

import androidx.appcompat.app.AppCompatActivity;
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
    FloatingActionButton deleteButton, editButton;
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
        detailQuantity = findViewById(R.id.detailQuantity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
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
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Log.d("DetailActivity", "File has been remove from Firebase Storage");
                        }).addOnFailureListener(e -> {
                            Log.e("DetailActivity", "Lỗi khi xóa file ảnh: " + e.getMessage());
                        });
                    }

                    runOnUiThread(() -> {
                        // Cập nhật UI trên main thread sau khi xóa sản phẩm
                        Toast.makeText(DetailActivity.this, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailActivity.this, ProductList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                });
            }
        });



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", bundle.getString("Title"))
                        .putExtra("Description", bundle.getString("Description"))
                        .putExtra("Price", bundle.getLong("Price"))
                        .putExtra("Quantity",bundle.getInt("Quantity"))
                        .putExtra("Image", imageUrl);
                startActivity(intent);
            }
        });
    }
}
