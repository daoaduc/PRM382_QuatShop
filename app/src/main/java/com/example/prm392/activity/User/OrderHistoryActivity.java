package com.example.prm392.activity.User;

import android.content.Intent;
import android.os.Bundle;

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

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;  // Assuming you have an Order model
    OrderDAO orderDAO;
    ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderDAO = new OrderDAO();
        Intent intent = getIntent();
        Account account = (Account) intent.getSerializableExtra("account");
        assert account != null;
        fetchOrderData(account);
    }

    public void fetchOrderData(Account account){
        executorService.submit(() -> {
            if (account.getRoleID() == 0) {
                // Fetch orders for a specific account
                orderList = orderDAO.getOrderByAccount(account.getAccID());
            } else if (account.getRoleID() == 1) {
                // Fetch all orders for admin
                orderList = orderDAO.getAllOrders();
            }

            // Update the UI on the main thread
            runOnUiThread(() -> {
                orderAdapter = new OrderAdapter(orderList);
                orderRecyclerView.setAdapter(orderAdapter);
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
