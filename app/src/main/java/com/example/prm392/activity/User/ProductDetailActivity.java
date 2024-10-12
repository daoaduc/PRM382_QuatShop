package com.example.prm392.activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.ConnectionClass;
import com.example.prm392.R;
import com.example.prm392.model.Product;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productPrice, productDetails;
    private EditText productQuantity;
    private Button addToCartButton, buyNowButton;
    ConnectionClass connectionClass;
    Connection con;
    ResultSet rs;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        connectionClass = new ConnectionClass();
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDetails = findViewById(R.id.product_details);
        productQuantity = findViewById(R.id.product_quantity);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_button);

        Intent intent = getIntent();
        int productID = intent.getIntExtra("productID", 1);

        String sql = "GET * FROM product WHERE productID = ?";

        try{
            PreparedStatement st = connectionClass.CONN().prepareStatement(sql);
            st.setInt(1, productID);
            rs = st.executeQuery();
            if(rs.next()){
                productName.setText(rs.getString("productName"));
                productPrice.setText("đ" + rs.getDouble("price"));
                productDetails.setText(rs.getString("description"));
                Picasso.get().load(rs.getString("productIMG")).into(productImage);
            }else{
                Toast.makeText(ProductDetailActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
