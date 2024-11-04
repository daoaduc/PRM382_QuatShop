package com.example.prm392.activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.prm392.ConnectionClass;
import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.activity.Admin.DashboardActivity;
import com.example.prm392.activity.Admin.OrderList;
import com.example.prm392.activity.Admin.ProductList;
import com.example.prm392.model.Cart;
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
    private Button addToCartButton;
    private ImageButton btnDecrease, btnIncrease;
    private ExecutorService executorService;
    CartDAO cartDAO;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDetails = findViewById(R.id.product_details);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        productQuantity = findViewById(R.id.etQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        executorService = Executors.newSingleThreadExecutor();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });

        CartDatabase db = CartDatabase.getInstance(this);  // Assuming you have a singleton instance of AppDatabase
        cartDAO = db.cartDAO();

        Intent intent = getIntent();
        int productID = intent.getIntExtra("productID", -1);

        fetchProductDetails(productID);
        btnDecrease.setOnClickListener(view -> decreaseQuantity());
        btnIncrease.setOnClickListener(view -> increaseQuantity());

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = getQuantity();  // Khai báo quantity từ phương thức getQuantity()
                if (quantity > 0) {
                    executorService.submit(() -> {
                        int userId = getCurrentUserId();  // Lấy userId từ SharedPreferences
                        Log.d("ProductDetailActivity", "userId: " + userId);
                        ProductDAO pd = new ProductDAO();
                        Product product = pd.getProductById(productID);
                        // Kiểm tra nếu product khác null trước khi lấy các thuộc tính của sản phẩm
                        if (product != null) {
                            Cart cartItem = new Cart();
                            cartItem.setProductId(productID);
                            cartItem.setProductName(product.getProductName());
                            cartItem.setPrice(product.getPrice());
                            cartItem.setQuantity(quantity);
                            cartItem.setImage(product.getProductIMG());
                            cartItem.setUserId(userId);  // Gán userId vào Cart

                            // Check if the item is already in the cart
                            new Thread(() -> {
                                Cart existingCartItem = cartDAO.getCartItemByProductIdAndUserId(productID, userId);
                                if (existingCartItem != null) {
                                    // Update the quantity of the existing item
                                    existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                                    cartDAO.update(existingCartItem);
                                } else {
                                    // Insert the new item into the cart
                                    cartDAO.insert(cartItem);
                                }
                                runOnUiThread(() -> {
                                    Toast.makeText(ProductDetailActivity.this, "Đã thêm " + quantity + " sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                    sendCartUpdateBroadcast();
                                });
                            }).start();
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(ProductDetailActivity.this, "Không thể thêm", Toast.LENGTH_SHORT).show();
                                finish();  // Close the activity if the product is not found
                            });
                        }
                    });
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Phải có ít nhất 1 sản phẩm", Toast.LENGTH_SHORT).show();
                }}
        });
    }
    private int getCurrentUserId() {
        SharedPreferences sharedPref = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        return sharedPref.getInt("userID", -1);  // Trả về -1 nếu không tìm thấy userId
    }

    private void sendCartUpdateBroadcast() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("com.example.prm392.CART_UPDATE");
        localBroadcastManager.sendBroadcast(intent);
        Log.d("ProductDetailActivity", "Broadcast CART_UPDATE sent from ProductDetailActivity.");
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
    private int getQuantity() {
        String quantityText = productQuantity.getText().toString();
        try {
            return Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            return 1;  // Default quantity if parse fails
        }
    }

    // Decrease the quantity
    private void decreaseQuantity() {
        int quantity = getQuantity();
        if (quantity > 1) {
            productQuantity.setText(String.valueOf(quantity - 1));
        }
    }

    // Increase the quantity
    private void increaseQuantity() {
        int quantity = getQuantity();
        productQuantity.setText(String.valueOf(quantity + 1));
    }
}