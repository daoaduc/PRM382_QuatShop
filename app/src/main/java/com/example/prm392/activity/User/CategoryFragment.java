package com.example.prm392.activity.User;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.prm392.DAO.ProductDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.SubTabProductAdapter;
import com.example.prm392.common.Constants;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductCategory;
import com.example.prm392.common.OnFragmentNavigationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragment extends Fragment {
    private View view;
    private RecyclerView mProducts;
    private ExecutorService executorService;
    private LinearLayout mCategoryContainer;
    private int mCurrentCategory;
    private String mCurrentSearchText;
    private SubTabProductAdapter mProductAdapter;
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
        view = inflater.inflate(R.layout.fragment_category, container, false);
        executorService = Executors.newFixedThreadPool(5);

        mCurrentCategory = Constants.ALL_BTN_CATEGORY;

        if (getArguments() != null) {
            if (getArguments().containsKey("searchText")) {
                mCurrentSearchText = getArguments().getString("searchText", "");
            }
            if (getArguments().containsKey("categoryID")) {
                mCurrentCategory = getArguments().getInt("categoryID", Constants.ALL_BTN_CATEGORY);
            }
        }
        else {
            mCurrentSearchText = "";
        }
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setUpCategories();
        setUpProducts();
    }

    private void setUpCategories(){
        mCategoryContainer = view.findViewById(R.id.button_container);
        executorService.execute(() -> {
            ProductDAO productDAO = new ProductDAO();
            List<ProductCategory> categoryList = new ArrayList<>();
            ProductCategory allbtn = new ProductCategory();
            allbtn.setCategoryID(Constants.ALL_BTN_CATEGORY);
            allbtn.setCategoryName("All");
            categoryList.add(allbtn);
            categoryList.addAll(productDAO.getAllCategories());

            if (categoryList != null && !categoryList.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    int indexOfSelectedCategory = 0;
                    for (int i = 0; i < categoryList.size(); i++) {
                        Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.btn_category, mCategoryContainer, false);
                        int categoryID = categoryList.get(i).getCategoryID();
                        button.setTag(Integer.toString(categoryID));
                        button.setText(categoryList.get(i).getCategoryName());
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
                        if(mCurrentCategory == categoryID){
                            indexOfSelectedCategory = i;
                        }
                    }
                    if(mCategoryContainer.getChildCount() != 0){
                        resetCategoryButton();
                        Button selectedButton = ((Button) mCategoryContainer.getChildAt(indexOfSelectedCategory));
                        selectedButton.setSelected(true);
                    }
                });
            } else {
                Log.d("CATEGORY", "No categories found or connection failed.");
            }
        });
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