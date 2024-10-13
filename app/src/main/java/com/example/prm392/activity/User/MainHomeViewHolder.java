package com.example.prm392.activity.User;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.prm392.ConnectionClass;
import com.example.prm392.R;
import com.example.prm392.adapter.MainBestSellerAdapter;
import com.example.prm392.adapter.MainCategoryItemAdapter;
import com.example.prm392.adapter.MainProductsAdapter;
import com.example.prm392.model.MainCategoryItemModel;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.Product;

import java.util.ArrayList;

public class MainHomeViewHolder extends AbsMainViewHolder{
    private ImageSlider imageSlider;
    private RecyclerView mCategories;
    private RecyclerView mBestSellerProducts;
    private RecyclerView mProducts;
    private ConnectionClass connectionClass;
    public MainHomeViewHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

// Hàm này để set layout cho class này
    @Override
    protected int getLayoutId() {
        return R.layout.view_main_home;
    }

// Hàm này xử lý setting cho view (activity) này sẽ bắt đầu viết ở đây ( tương tự như hàm onCreate thường dùng )
// ví dụ:
//    connectionClass = new ConnectionClass();
//    edtFullname = findViewById(R.id.edtFullname)
    @Override
    public void init() {
        connectionClass = new ConnectionClass();

        imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(getImageSlider(), ScaleTypes.FIT);

//set data cho category
        MainCategoryItemAdapter categoryItemAdapter = new MainCategoryItemAdapter(mContext, getCategories());
        categoryItemAdapter.setOnItemClickListener(new OnItemClickListener<MainCategoryItemModel>() {
            @Override
            public void onItemClick(MainCategoryItemModel item, int position) {
                // change activity or change tab
                // ...
            }
        });
        mCategories = findViewById(R.id.categories);
        mCategories.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mCategories.setAdapter(categoryItemAdapter);

//set data cho best seller
        MainBestSellerAdapter bestSellerAdapter = new MainBestSellerAdapter(mContext, getBestSeller());
        bestSellerAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {

            }
        });
        mBestSellerProducts = findViewById(R.id.best_seller_products);
        mBestSellerProducts.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mBestSellerProducts.setAdapter(bestSellerAdapter);

//set data cho product
        MainProductsAdapter productsAdapter = new MainProductsAdapter(mContext, getProducts());
        productsAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {

            }
        });
        mProducts = findViewById(R.id.products);
        mProducts.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mProducts.setAdapter(productsAdapter);
    }

// Hàm này sẽ tự động được gọi khi chuyển sang tab này
// Đây là nơi xử lý data cho view này nhé
    @Override
    public void loadData(){

    }

    private ArrayList<SlideModel> getImageSlider(){
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slide_1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_3, ScaleTypes.FIT));

        return slideModels;
    }

    private ArrayList<MainCategoryItemModel> getCategories(){
        ArrayList<MainCategoryItemModel> list = new ArrayList<>();

        list.add(new MainCategoryItemModel(1, "mom"));
        list.add(new MainCategoryItemModel(2, "daddy"));
        list.add(new MainCategoryItemModel(3, "baby"));

        return list;
    }

    private ArrayList<Product> getBestSeller() {
        ArrayList<Product> list = new ArrayList<>();

        list.add(new Product());
        list.add(new Product());
        list.add(new Product());
        return list;
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