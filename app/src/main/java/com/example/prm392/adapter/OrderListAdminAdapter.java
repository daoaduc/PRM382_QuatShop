package com.example.prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.activity.Admin.DetailActivity;
import com.example.prm392.activity.Admin.OrderDetail;
import com.example.prm392.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderListAdminAdapter extends RecyclerView.Adapter<OrderListAdminAdapter.MyViewHolder> {
    private Context context;
    private List<Order> dataList;

    public OrderListAdminAdapter(Context context, List<Order> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public OrderListAdminAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderListAdminAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdminAdapter.MyViewHolder holder, int position) {
        Order order = dataList.get(position);
        holder.orderCode.setText(dataList.get(position).getOrderCode());
        double totalMoney = dataList.get(position).getTotalMoney();
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(totalMoney) + " VND";
        holder.totalMoney.setText("Số lượng còn: " + formattedPrice);

        String status = dataList.get(position).getStatus().getStatus();
        holder.status.setText(status);
        Log.d("StatusOrderlist", "Status: " + status);



        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetail.class);
                intent.putExtra("OrderID", order.getOrderID());
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Order> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderCode, totalMoney,status;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recCard = itemView.findViewById(R.id.recCard);
            totalMoney = itemView.findViewById(R.id.totalMoney);
            orderCode = itemView.findViewById(R.id.orderCode);
            status = itemView.findViewById(R.id.status);
        }
    }
}

