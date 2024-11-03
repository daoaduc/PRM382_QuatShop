package com.example.prm392.activity.User;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.MainBestSellerAdapter;
import com.example.prm392.adapter.MainCategoryAdapter;
import com.example.prm392.adapter.MainProductAdapter;
import com.example.prm392.common.OnFragmentNavigationListener;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private View view;
    private ImageSlider imageSlider;
    private RecyclerView mCategories;
    private RecyclerView mBestSellerProducts;
    private RecyclerView mProducts;
    private ExecutorService executorService;
    private OnFragmentNavigationListener navigationListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            navigationListener = (OnFragmentNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentNavigationListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        executorService = Executors.newFixedThreadPool(5);
        setupSlide();
        setupCategory();
        setupBestSeller();
        setupProducts();
        setupMapFragment();
        return view;
    }

    private void setupSlide() {
        imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(getImageSlider(), ScaleTypes.FIT);
    }

    private ArrayList<SlideModel> getImageSlider() {
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide_1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_3, ScaleTypes.FIT));
        return slideModels;
    }

    private void setupBestSeller() {
        MainBestSellerAdapter bestSellerAdapter = new MainBestSellerAdapter(getContext());
        setBestSellerData(bestSellerAdapter);
        bestSellerAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {
                Log.d("ITEM_CLICK", "Product clicked: " + item.getProductName());
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("productID", item.getProductID());
                startActivity(intent);
            }
        });

        // Find the RecyclerView for best sellers and set its adapter and layout manager
        mBestSellerProducts = view.findViewById(R.id.best_seller_products);
        mBestSellerProducts.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mBestSellerProducts.setAdapter(bestSellerAdapter);
    }

    // Fetch the top 3 bestseller products
    private void setBestSellerData(MainBestSellerAdapter adapter) {
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<Product> productList = productDAO.getTop3BestSellers();

            if (productList != null && !productList.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    adapter.setList(productList);
                });
            } else {
                Log.d("PRODUCT", "No products found or connection failed.");
            }
        });
    }

    private void setupCategory() {
        MainCategoryAdapter categoryAdapter = new MainCategoryAdapter(getContext());

        // Fetch the category data
        setCategoryData(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new OnItemClickListener<ProductCategory>() {
            @Override
            public void onItemClick(ProductCategory item, int position) {
                Log.d("CATEGORY_CLICK", "Category clicked: " + item.getCategoryID());
                // Create a bundle to send the category ID to CategoryFragment
                Bundle bundle = new Bundle();
                bundle.putInt("categoryID", item.getCategoryID());

                navigationListener.navigateToFragment(new CategoryFragment(), "Category", bundle);
            }
        });

        // Find the RecyclerView for categories and set its adapter and layout manager
        mCategories = view.findViewById(R.id.categories);
        mCategories.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mCategories.setAdapter(categoryAdapter);
    }

    // Fetch categories and update the adapter
    private void setCategoryData(MainCategoryAdapter adapter) {
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

    private void setupProducts() {
        // Initialize the product adapter
        MainProductAdapter productAdapter = new MainProductAdapter(getContext());

        // Fetch the product data
        setProductData(productAdapter);

        // Set click listener for each product item
        productAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {
                // Handle what happens when a product is clicked
                Log.d("PRODUCT_CLICK", "Product clicked: " + item.getProductName());
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("productID", item.getProductID());
                startActivity(intent);
            }
        });

        // Find the RecyclerView for products and set its adapter and layout manager
        mProducts = view.findViewById(R.id.products);
        mProducts.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mProducts.setAdapter(productAdapter);
    }

    // Fetch all products from the database and update the adapter
    private void setProductData(MainProductAdapter adapter) {
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<Product> productList = productDAO.getAllProducts2(); // Implemented in ProductDAO

            if (productList != null && !productList.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    adapter.setList(productList);
                });
            } else {
                Log.d("PRODUCT", "No products found or connection failed.");
            }
        });
    }

    private void setupMapFragment() {
        Fragment mapFragment = new MapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment).commit();
    }
}