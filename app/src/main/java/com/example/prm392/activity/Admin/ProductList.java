package com.example.prm392.activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.ProductListAdminAdapter;
import com.example.prm392.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductList extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ProductListAdminAdapter adapter;
    private List<Product> dataList; // Danh sách chứa tất cả sản phẩm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crud);

        fab = findViewById(R.id.fab);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo SearchView
        SearchView searchView = findViewById(R.id.search);

        // Khởi tạo ExecutorService để xử lý trên background thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Thực thi task trong background thread
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<Product> productList = productDAO.getAllProducts();

            if (productList != null && !productList.isEmpty()) {
                runOnUiThread(() -> {
                    dataList = productList;
                    adapter = new ProductListAdminAdapter(this, dataList);
                    recyclerView.setAdapter(adapter);
                });
            } else {
                Log.d("PRODUCT", "No products found or connection failed.");
            }
        });

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

        // Đừng quên shutdown executor service sau khi hoàn thành
        executorService.shutdown();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductList.this, UploadActivity.class);
                startActivity(intent);
            }
        });
    }

    public void searchList(String text) {
        ArrayList<Product> searchList = new ArrayList<>();
        for (Product product : dataList) {
            if (product.getProductName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(product);
            }
        }
        adapter.searchDataList(searchList);
    }
}