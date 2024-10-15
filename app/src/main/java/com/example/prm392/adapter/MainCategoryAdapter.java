package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.ProductCategory;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.Vh> {

    private List<ProductCategory> mList;
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<ProductCategory> mOnItemClickListener;

    public MainCategoryAdapter(Context context, List<ProductCategory> list) {
        mContext = context;
        mList = list;
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
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(mContext).inflate(R.layout.item_main_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        ProductCategory category = mList.get(position);
        holder.categoryName.setText(category.getCategoryName());

        Glide.with(mContext)
                .load(category.getCategoryIMG())  // Load image from URL or drawable
                .placeholder(R.drawable.uploadimg) // Placeholder image while loading
                .into(holder.categoryImage);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener<ProductCategory> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class Vh extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }

    public void setList(List<ProductCategory> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
