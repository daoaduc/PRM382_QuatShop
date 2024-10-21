package com.example.prm392.activity.User;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.OrderDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.OrderAdapter;
import com.example.prm392.model.Order;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;  // Assuming you have an Order model
    OrderDAO orderDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderDAO = new OrderDAO();
        //Check role user
        //Admin -> use method getAllOrders()
        //User -> use method getOrderByAccount()

        orderAdapter = new OrderAdapter(orderList);
        orderRecyclerView.setAdapter(orderAdapter);
    }
}
