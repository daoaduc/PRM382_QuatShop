package com.example.prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.activity.Admin.DetailActivity;
import com.example.prm392.model.Product;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdminAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Product> dataList;

    public ProductListAdminAdapter(Context context, List<Product> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = dataList.get(position);
        holder.recTitle.setText(dataList.get(position).getProductName());
        int quantity = dataList.get(position).getQuantity();
        holder.recQuantity.setText("Số lượng còn: " + String.valueOf(quantity));

        String status = dataList.get(position).getStatus().getStatusName();
        holder.recStatus.setText(status);

        Glide.with(context)
                .load(product.getProductIMG()) // Đường dẫn hình ảnh từ Firebase
                .placeholder(R.drawable.uploadimg) // Hình ảnh thay thế khi đang tải
                .into(holder.recImage);


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = product.getDescription();
                Log.d("ProductDescription", "Description: " + description);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ProductID", product.getProductID());
                intent.putExtra("Price", product.getPrice());
                intent.putExtra("Image", product.getProductIMG());
                intent.putExtra("Description", description);
                intent.putExtra("Title", product.getProductName());
                intent.putExtra("Status", product.getStatus().getStatusName());
                int quantity = product.getQuantity();
                intent.putExtra("Quantity", quantity);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Product> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView recImage;
    TextView recTitle, recQuantity,recStatus;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recQuantity = itemView.findViewById(R.id.recQuantity);
        recTitle = itemView.findViewById(R.id.recTitle);
        recStatus = itemView.findViewById(R.id.recStatus);
    }
}
