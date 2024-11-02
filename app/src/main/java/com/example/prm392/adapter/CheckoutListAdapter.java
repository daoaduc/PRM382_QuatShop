package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.model.Cart;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutListAdapter extends RecyclerView.Adapter<CheckoutListAdapter.ViewHolder> {
    private Context context;
    private List<Cart> cartList;

    public CheckoutListAdapter(Context context, List<Cart> cartItems) {
        this.context = context;
        this.cartList = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        holder.recTitle.setText(cart.getProductName());
        holder.recQuantity.setText("x" + cart.getQuantity());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = "₫" + formatter.format(cart.getPrice());
        holder.recPrice.setText(formattedPrice);

        Glide.with(holder.itemView.getContext())
                .load(cart.getImage())
                .placeholder(R.drawable.img)
                .error(R.drawable.uploadimg)
                .into(holder.recImage);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView recImage;
        TextView recTitle, recPrice, recQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.recImage); // Khởi tạo recImage
            recTitle = itemView.findViewById(R.id.recTitle);
            recPrice = itemView.findViewById(R.id.recPrice); // Khởi tạo recPrice
            recQuantity = itemView.findViewById(R.id.recQuantity); // Khởi tạo recQuantity
        }
    }
}
