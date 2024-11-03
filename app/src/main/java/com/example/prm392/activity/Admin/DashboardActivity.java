package com.example.prm392.activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.R;
import com.example.prm392.activity.User.MainActivity2;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Tìm các LinearLayout theo ID
        LinearLayout productManagement = findViewById(R.id.product_management);
        LinearLayout orderManagement = findViewById(R.id.order_management);
        LinearLayout shop = findViewById(R.id.shop);

        // Thiết lập sự kiện nhấn cho Product Management
        productManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện nhấn vào Product Management
                Intent intent = new Intent(DashboardActivity.this, ProductList.class);
                startActivity(intent);
            }
        });

        // Thiết lập sự kiện nhấn cho Order Management
        orderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện nhấn vào Order Management
                Intent intent = new Intent(DashboardActivity.this, OrderList.class);
                startActivity(intent);
            }
        });

        // Thiết lập sự kiện nhấn cho Shop
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện nhấn vào Shop
                Intent intent = new Intent(DashboardActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}
