package com.example.prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderId.setText("Order ID: " + order.getOrderID());
        holder.orderCode.setText("Order Code: " + order.getOrderCode());
        holder.username.setText("Username: " + order.getAccID());
        holder.totalMoney.setText("Total: $" + order.getTotalMoney());
        holder.paymentMethod.setText("Payment Method: " + order.getPaymentMethod());
        holder.orderDate.setText("Order Date: " + order.getOrderDate());
        holder.confirmedDate.setText("Confirmed Date: " + order.getConfirmedDate());
        holder.pickupDate.setText("Pickup Date: " + order.getPickUpDate());
        holder.deliveryDate.setText("Delivery Date: " + order.getDeliveryDate());
        holder.status.setText("Status: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, orderCode, username, totalMoney, paymentMethod, orderDate, confirmedDate, pickupDate, deliveryDate, status;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCode = itemView.findViewById(R.id.orderCode);
            username = itemView.findViewById(R.id.username);
            totalMoney = itemView.findViewById(R.id.totalMoney);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            orderDate = itemView.findViewById(R.id.orderDates);
            confirmedDate = itemView.findViewById(R.id.confirmedDate);
            pickupDate = itemView.findViewById(R.id.pickupDate);
            deliveryDate = itemView.findViewById(R.id.deliveryDate);
            status = itemView.findViewById(R.id.status);
        }
    }
}

