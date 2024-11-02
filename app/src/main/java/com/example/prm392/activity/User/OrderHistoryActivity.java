package com.example.prm392.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.OrderDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.OrderAdapter;
import com.example.prm392.model.Account;
import com.example.prm392.model.Order;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;  // Assuming you have an Order model
    private OrderDAO orderDAO;
    private ImageButton btnBack;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Initialize RecyclerView and layout manager
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btn_back_order);
        // Initialize OrderDAO and ExecutorService
        orderDAO = new OrderDAO();
        executorService = Executors.newSingleThreadExecutor();
        btnBack.setOnClickListener(v -> onBackPressed());
        // Get Account object from Intent
        Intent intent = getIntent();
        Account account = (Account) intent.getSerializableExtra("account");

        // Ensure account is not null
        if (account != null) {
            Log.d("OrderHistoryActivity", "Account Role ID: " + account.getRoleID());
            fetchOrderData(account);
        } else {
            Toast.makeText(this, "Account null", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchOrderData(Account account) {
        executorService.submit(() -> {
            if (account.getRoleID() == 2) {
                // Fetch orders for a specific account
                orderList = orderDAO.getOrderByAccount(account.getAccID());
                Log.d("Orders", "Number of orders: " + orderList.size());
            } else if (account.getRoleID() == 1) {
                // Fetch all orders for admin
                orderList = orderDAO.getAllOrders();
                Log.d("Orders", "Number of orders: " + orderList.size());
            }

            // Update the UI on the main thread
            runOnUiThread(() -> {
                TextView tvNoOrders = findViewById(R.id.tv_no_orders); // Re-initialize TextView

                if (orderList != null && !orderList.isEmpty()) {
                    orderAdapter = new OrderAdapter(orderList);
                    orderRecyclerView.setAdapter(orderAdapter);
                    orderRecyclerView.setVisibility(View.VISIBLE); // Show RecyclerView
                    tvNoOrders.setVisibility(View.GONE); // Hide "No orders" message
                } else {
                    orderRecyclerView.setVisibility(View.GONE); // Hide RecyclerView
                    tvNoOrders.setVisibility(View.VISIBLE); // Show "No orders" message
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shut down ExecutorService to avoid memory leaks
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
