package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class SubTabCategoryAdapter extends RecyclerView.Adapter<SubTabCategoryAdapter.Vh> {
    private List<ProductCategory> mList;
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<ProductCategory> mOnItemClickListener;

    public SubTabCategoryAdapter(Context context) {
        mContext = context;
        if (mList == null){
            mList = new ArrayList<ProductCategory>();
        }
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    ProductCategory item = mList.get(position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(item, position);
                    }
                }
            }
        };
    }

    @NonNull
    @Override
    public SubTabCategoryAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubTabCategoryAdapter.Vh(LayoutInflater.from(mContext).inflate(R.layout.btn_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubTabCategoryAdapter.Vh holder, int position) {
        ProductCategory category = mList.get(position);
        holder.categoryBtn.setText(category.getCategoryName());
        holder.categoryBtn.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener<ProductCategory> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class Vh extends RecyclerView.ViewHolder {
        Button categoryBtn;

        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
            categoryBtn = itemView.findViewById(R.id.categoryBtn);
        }
    }

    public void setList(List<ProductCategory> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
