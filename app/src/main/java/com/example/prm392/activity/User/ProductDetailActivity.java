package com.example.prm392.activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.ConnectionClass;
import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.model.Product;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productPrice, productDetails;
    private EditText productQuantity;
    private Button addToCartButton, buyNowButton;
    private ExecutorService executorService;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDetails = findViewById(R.id.product_details);
        productQuantity = findViewById(R.id.product_quantity);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_button);

        executorService = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        int productID = intent.getIntExtra("productID", 1);

        fetchProductDetails(productID);

        ProductDAO pd = new ProductDAO();
        Product product = pd.getProductById(productID);
//        if (product != null) {
//            productName.setText(product.getProductName());
//            productPrice.setText("đ" + product.getPrice());
//            productDetails.setText(product.getDescription());
//            Picasso.get().load(product.getProductIMG()).into(productImage);
//        } else {
//            Toast.makeText(ProductDetailActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
//            finish();  // Optionally close the activity if product is not found
//        }

        addToCartButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));
        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void fetchProductDetails(int productID) {
        // Execute database operation in background
        Future<?> future = executorService.submit(() -> {
            ProductDAO pd = new ProductDAO();
            Product product = pd.getProductById(productID);

            runOnUiThread(() -> {
                if (product != null) {
                    // Update UI with product details
                    productName.setText(product.getProductName());
                    productPrice.setText("đ" + product.getPrice());
                    productDetails.setText(product.getDescription());
                    Picasso.get().load(product.getProductIMG()).into(productImage);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity if the product is not found
                }
            });
        });
    }
}
