package com.example.prm392.activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.ConnectionClass;
import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
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
                int quantity = getQuantity();
                if (quantity > 0) {
                    // Execute database operation in background
                    executorService.submit(() -> {
                        ProductDAO pd = new ProductDAO();
                        Product product = pd.getProductById(productID);

                        if (product != null) {
                            Cart cartItem = new Cart();
                            cartItem.setProductId(productID);
                            cartItem.setProductName(product.getProductName());
                            cartItem.setPrice(product.getPrice());
                            cartItem.setQuantity(quantity);
                            cartItem.setImage(product.getProductIMG());

                            // Insert item into cart without assigning the ID to a variable
                            cartDAO.insert(cartItem);

                            // Run on UI thread to display Toast after insertion
                            runOnUiThread(() ->
                                    Toast.makeText(ProductDetailActivity.this, "Added " + quantity + " items to cart", Toast.LENGTH_SHORT).show()
                            );
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(ProductDetailActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                                finish();  // Close the activity if the product is not found
                            });
                        }
                    });
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Quantity must be at least 1", Toast.LENGTH_SHORT).show();
                }
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
