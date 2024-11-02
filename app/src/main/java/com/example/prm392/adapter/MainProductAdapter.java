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
import com.example.prm392.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainProductAdapter extends RecyclerView.Adapter<MainProductAdapter.Vh> {

    private List<Product> mList;
    // Context to access resources and other app-related assets
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<Product> mOnItemClickListener;

    // initialize context and list
    public MainProductAdapter(Context context) {
        mContext = context;
        if (mList == null) {
            mList = new ArrayList<Product>(); // Initialize product list if null
        }
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();  // Retrieve tag (position) from the clicked view
                if (tag != null) {
                    int position = (int) tag;  // Get the position of the clicked item
                    Product item = mList.get(position);  // Get the product at the clicked position
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
        // Inflate the layout for each item in the RecyclerView
        return new Vh(LayoutInflater.from(mContext).inflate(R.layout.item_main_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        // Get the product at the given position
        Product product = mList.get(position);
        holder.productTitle.setText(product.getProductName());

        String formattedPrice = NumberFormat.getInstance(new Locale("vi", "VN")).format(product.getPrice());
        holder.productPrice.setText(formattedPrice + " ₫");

        Glide.with(mContext)
                .load(product.getProductIMG())  // Load image from URL or Firebase
                .placeholder(R.drawable.uploadimg) // Placeholder image while loading
                .into(holder.productImage);  // Set the loaded image into the ImageView

        // Set the tag as the position of the item, can retrieve it later when the item is clicked
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    // Method to set the click listener from the outside (activity or fragment)
    public void setOnItemClickListener(OnItemClickListener<Product> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    // ViewHolder class: holds the views for each item (reused as the user scrolls)
    public class Vh extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle, productPrice;

        // ViewHolder links the views in the layout to this class
        public Vh(View itemView) {
            super(itemView);
            // Set the click listenr for the entire item view
            itemView.setOnClickListener(mOnClickListener);
            productImage = itemView.findViewById(R.id.recImage);
            productTitle = itemView.findViewById(R.id.title);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    public void setList(List<Product> list) {
        mList = list;  // Update the product list
        notifyDataSetChanged();  // Notify RecyclerView to refresh the UI with new data
    }
}
