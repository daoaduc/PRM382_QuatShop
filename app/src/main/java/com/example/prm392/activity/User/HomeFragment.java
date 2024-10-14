package com.example.prm392.activity.User;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.prm392.adapter.MainCategoryItemAdapter;
import com.example.prm392.adapter.MainProductsAdapter;
import com.example.prm392.adapter.ProductListAdminAdapter;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.MainCategoryItemModel;
import com.example.prm392.model.Product;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setupSlide();
        setupCategory();
        setupBestSeller();
        setupProducts();
        return view;
    }

    private void setupSlide(){
        imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(getImageSlider(), ScaleTypes.FIT);
    }

    private ArrayList<SlideModel> getImageSlider(){
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slide_1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_3, ScaleTypes.FIT));

        return slideModels;
    }

    private void setupCategory(){
        MainCategoryItemAdapter categoryItemAdapter = new MainCategoryItemAdapter(getContext(), getCategories());
        categoryItemAdapter.setOnItemClickListener(new OnItemClickListener<MainCategoryItemModel>() {
            @Override
            public void onItemClick(MainCategoryItemModel item, int position) {
                // change activity or change tab
                // ...
            }
        });
        mCategories = view.findViewById(R.id.categories);
        mCategories.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mCategories.setAdapter(categoryItemAdapter);
    }

    private void setupBestSeller() {
        MainBestSellerAdapter bestSellerAdapter = new MainBestSellerAdapter(getContext());
        setBestSellerData(bestSellerAdapter);
        bestSellerAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {

            }
        });
        mBestSellerProducts = view.findViewById(R.id.best_seller_products);
        mBestSellerProducts.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mBestSellerProducts.setAdapter(bestSellerAdapter);
    }

    private void setupProducts(){
        MainProductsAdapter productsAdapter = new MainProductsAdapter(getContext(), getProducts());
        productsAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {

            }
        });
        mProducts = view.findViewById(R.id.products);
        mProducts.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mProducts.setAdapter(productsAdapter);
    }

    private ArrayList<MainCategoryItemModel> getCategories(){
        ArrayList<MainCategoryItemModel> list = new ArrayList<>();

        list.add(new MainCategoryItemModel(1, "mom"));
        list.add(new MainCategoryItemModel(2, "daddy"));
        list.add(new MainCategoryItemModel(3, "baby"));

        return list;
    }

    private void setBestSellerData(MainBestSellerAdapter adapter) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Thực thi task trong background thread
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<Product> productList = productDAO.getAllProducts();

            if (productList != null && !productList.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    adapter.setList(productList);
                });
            } else {
                Log.d("PRODUCT", "No products found or connection failed.");
            }
        });
    }

    private ArrayList<Product> getProducts() {
        ArrayList<Product> list = new ArrayList<>();

        list.add(new Product());
        list.add(new Product());
        list.add(new Product());
        list.add(new Product());
        list.add(new Product());
        list.add(new Product());
        list.add(new Product());
        list.add(new Product());

        return list;
    }
}