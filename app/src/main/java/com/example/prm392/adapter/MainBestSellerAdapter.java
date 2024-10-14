package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.common.OnItemClickListener;
import com.example.prm392.model.MainCategoryItemModel;
import com.example.prm392.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainBestSellerAdapter extends RecyclerView.Adapter<MainBestSellerAdapter.Vh>{

    private List<Product> mList;
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<Product> mOnItemClickListener;

    public MainBestSellerAdapter(Context context){
        mContext = context;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mList != null && mList.size() > 0) {
                    int position = (int) tag;
                    Product item = mList.get(position);
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
        return new Vh(LayoutInflater.from(mContext).inflate(R.layout.item_main_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        Product product = mList.get(position);
        holder.recTitle.setText(mList.get(position).getProductName());

        int price = mList.get(position).getQuantity();
        holder.recPrice.setText(String.valueOf(price));

        Glide.with(mContext)
                .load(product.getProductIMG()) // Đường dẫn hình ảnh từ Firebase
                .placeholder(R.drawable.uploadimg) // Hình ảnh thay thế khi đang tải
                .into(holder.recImage);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener<Product> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class Vh extends RecyclerView.ViewHolder{
        ImageView recImage;
        TextView recTitle, recPrice;

        public Vh(View itemView){
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
            recImage = itemView.findViewById(R.id.recImage);
            recPrice = itemView.findViewById(R.id.productPrice);
            recTitle = itemView.findViewById(R.id.title);
        }
    }

    public void setList(List<Product> list) {
        mList = list;
        notifyDataSetChanged();
    }
}