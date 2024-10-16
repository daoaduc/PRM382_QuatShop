package com.example.prm392.activity.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.SubTabCategoryAdapter;
import com.example.prm392.adapter.SubTabProductAdapter;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductCategory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragment extends Fragment {
    private View view;
    private RecyclerView mCategories;
    private RecyclerView mProducts;
    private ExecutorService executorService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        executorService = Executors.newFixedThreadPool(5);
        setUpCategories();
        setUpProducts();
        return view;
    }

    private void setUpCategories(){
        SubTabCategoryAdapter adapter = new SubTabCategoryAdapter(getContext());

        setCategoryData(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener<ProductCategory>() {
            @Override
            public void onItemClick(ProductCategory item, int position) {
                // Handle what happens when a category is clicked
                Log.d("CATEGORY_CLICK", "Category clicked: " + item.getCategoryID());
            }
        });

        // Find the RecyclerView for categories and set its adapter and layout manager
        mCategories = view.findViewById(R.id.category_buttons);
        mCategories.setLayoutManager(new LinearLayoutManager(getContext(),  LinearLayoutManager.HORIZONTAL, false));
        mCategories.setAdapter(adapter);
    }

    private void setCategoryData(SubTabCategoryAdapter adapter) {
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<ProductCategory> categoryList = productDAO.getAllCategories();

            if (categoryList != null && !categoryList.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    adapter.setList(categoryList);
                });
            } else {
                Log.d("CATEGORY", "No categories found or connection failed.");
            }
        });
    }

    private void setUpProducts(){
        SubTabProductAdapter adapter = new SubTabProductAdapter(getContext());

        setProductData(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {
                Log.d("PRODUCT_CLICK", "Product clicked: " + item.getProductName());
            }
        });

        mProducts = view.findViewById(R.id.recycler_view_products);
        mProducts.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mProducts.setAdapter(adapter);
    }

    private void setProductData(SubTabProductAdapter adapter) {
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<Product> productList = productDAO.getAllProducts2();

            if (productList != null && !productList.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    adapter.setList(productList);
                });
            } else {
                Log.d("PRODUCT", "No products found or connection failed.");
            }
        });
    }
}