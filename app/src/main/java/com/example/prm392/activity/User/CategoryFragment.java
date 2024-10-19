package com.example.prm392.activity.User;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.SubTabCategoryAdapter;
import com.example.prm392.adapter.SubTabProductAdapter;
import com.example.prm392.common.Constants;
import com.example.prm392.common.OnGetSearchProduct;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragment extends Fragment {
    private View view;
    private RecyclerView mCategories;
    private RecyclerView mProducts;
    private ExecutorService executorService;
    private int mCurrentIdBtn;
    private LinearLayout mCategoryContainer;
    private int mCurrentCategory;
    private String mCurrentSearchText;
    private SubTabProductAdapter mProductAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        executorService = Executors.newFixedThreadPool(5);
        if(getArguments() != null){
            mCurrentSearchText = getArguments().getString("searchText");
        } else {
            mCurrentSearchText = "";
        }
        setUpCategories();
        setUpProducts();
        return view;
    }

    private void setUpCategories(){
        mCategoryContainer = view.findViewById(R.id.button_container);

        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<ProductCategory> categoryList = new ArrayList<>();
            ProductCategory allbtn = new ProductCategory();
            allbtn.setCategoryID(Constants.ALL_BTN_CATEGORY);
            mCurrentCategory = Constants.ALL_BTN_CATEGORY;
            allbtn.setCategoryName("All");
            categoryList.add(allbtn);
            categoryList.addAll(productDAO.getAllCategories());

            if (categoryList != null && !categoryList.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    for (ProductCategory p: categoryList) {
                        Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.btn_category, mCategoryContainer, false);
                        button.setTag(Integer.toString(p.getCategoryID()));
                        button.setText(p.getCategoryName());
                        mCategoryContainer.addView(button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                resetCategoryButton();
                                view.setSelected(true);
                                mCurrentCategory = Integer.parseInt((String) view.getTag());
                                setProductData();
                            }
                        });
                        if(mCategoryContainer.getChildCount() != 0){
                            resetCategoryButton();
                            Button firstButton = ((Button) mCategoryContainer.getChildAt(0));
                            firstButton.setSelected(true);
                        }
                    }
                });
            } else {
                Log.d("CATEGORY", "No categories found or connection failed.");
            }
        });
    }

    private void setCategoryData(SubTabCategoryAdapter adapter) {

    }

    private void setUpProducts(){
        mProductAdapter = new SubTabProductAdapter(getContext());
        setProductData();
        mProductAdapter.setOnItemClickListener(new OnItemClickListener<Product>() {
            @Override
            public void onItemClick(Product item, int position) {
                Log.d("PRODUCT_CLICK", "Product clicked: " + item.getProductName());
            }
        });

        mProducts = view.findViewById(R.id.recycler_view_products);
        mProducts.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mProducts.setAdapter(mProductAdapter);
    }

    private void setProductData() {
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<Product> productList = productDAO.getProductsBySearching(mCurrentSearchText, mCurrentCategory);

            if (productList != null) {
                getActivity().runOnUiThread(() -> {
                    mProductAdapter.setList(productList);
                });
            } else {
                Log.d("PRODUCT", "No products found or connection failed.");
            }
        });
    }

    private void resetCategoryButton(){
        for (int i = 0; i < mCategoryContainer.getChildCount(); i++) {
            Button btn = (Button) mCategoryContainer.getChildAt(i);
            btn.setSelected(false);
        }
    }

    public void updateData(Bundle args) {
        if (args != null) {
            String searchText = args.getString("searchText");
            if (mCurrentSearchText != null && searchText != null && mCurrentSearchText != searchText ) {
                mCurrentSearchText = searchText;
                setProductData();
            }
        }
    }
}