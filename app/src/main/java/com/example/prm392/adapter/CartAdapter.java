package com.example.prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnQuantityChangeListener onQuantityChangeListener;

    // Interface for delete click listener
    public interface OnDeleteClickListener {
        void onDeleteClick(Cart cart);
    }

    // Interface for quantity change listener
    public interface OnQuantityChangeListener {
        void onQuantityChange(Cart cart, int quantity);
    }

    // Constructor for setting both listeners
    public CartAdapter(List<Cart> cartList, OnDeleteClickListener onDeleteClickListener, OnQuantityChangeListener onQuantityChangeListener) {
        this.cartList = cartList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart currentItem = cartList.get(position);

        // Use Glide to load the product image
        Glide.with(holder.itemView.getContext())
                .load(currentItem.getImage()) // URL or file path
                .placeholder(R.drawable.img) // Placeholder while loading
                .error(R.drawable.error_image) // Error image
                .into(holder.imgProduct);

        // Bind the product name, price, and quantity
        holder.txtProductName.setText(currentItem.getProductName());
        holder.txtProductPrice.setText(String.format("$%s", currentItem.getPrice()));
        holder.txtQuantity.setText(String.valueOf(currentItem.getQuantity()));

        // Increase quantity button
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = currentItem.getQuantity() + 1;
            if (onQuantityChangeListener != null) {
                onQuantityChangeListener.onQuantityChange(currentItem, newQuantity);
            }
        });

        // Decrease quantity button
        holder.btnDecrease.setOnClickListener(v -> {
            if (currentItem.getQuantity() > 1) {
                int newQuantity = currentItem.getQuantity() - 1;
                if (onQuantityChangeListener != null) {
                    onQuantityChangeListener.onQuantityChange(currentItem, newQuantity);
                }
            }
        });

        // Delete button
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // ViewHolder class for each cart item
    class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView txtProductName;
        private TextView txtProductPrice;
        private TextView txtQuantity;
        private ImageView imgProduct;
        private ImageButton btnIncrease;
        private ImageButton btnDecrease;
        private ImageButton btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
