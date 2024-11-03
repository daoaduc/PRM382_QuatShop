package com.example.prm392.activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.OrderDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.OrderListAdminAdapter;
import com.example.prm392.adapter.ProductListAdminAdapter;
import com.example.prm392.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderListAdminAdapter adapter;
    private List<Order> dataList; // Danh sách chứa tất cả sản phẩm

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orderlist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(OrderList.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.search);

        loadOrders();

        // Thiết lập listener cho SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });

    }

    private void loadOrders() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orderList = orderDAO.getAllOrders();

            runOnUiThread(() -> {
                if (orderList != null && !orderList.isEmpty()) {
                    dataList = orderList;
                    adapter = new OrderListAdminAdapter(this, dataList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("PRODUCT", "No products found or connection failed.");
                }
            });
        });
    }

    public void searchList(String text) {
        ArrayList<Order> searchList = new ArrayList<>();
        for (Order order : dataList) {
            if (order.getOrderCode().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(order);
            }
        }
        adapter.searchDataList(searchList);
    }
}
