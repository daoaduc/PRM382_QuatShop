package com.example.prm392.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.R;
import com.example.prm392.adapter.CartAdapter;
import com.example.prm392.model.Cart;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class CartFragment extends Fragment {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private CartDAO cartDAO;
    List<Cart> cartItems;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerViewCart = view.findViewById(R.id.cartRecycleView);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách cartItems
        cartItems = new ArrayList<>();
        cartDAO = CartDatabase.getInstance(getContext()).cartDAO();
        new Thread(() -> {
            int userId = getUserId();  // Lấy userId từ SharedPreferences
            Log.d("ProductDetailActivity", "userId: " + userId);
            for(Cart item : cartDAO.getAllCartItemsByUserId(userId))
            {
                cartItems.add(item);
            }}).start();

        // Khởi tạo adapter với cả hai listener
        cartAdapter = new CartAdapter(cartItems, cart -> {
            new Thread(() -> {
                cartDAO.delete(cart);
                cartItems.remove(cart);
                // Check if the cart is now empty
                if (cartItems.isEmpty()) {
                    sendCartUpdateBroadcast(); // Send broadcast if cart is empty
                }
                calculatePrice(); // Call calculatePrice() after deleting an item
            }).start();
            getActivity().runOnUiThread(() -> {
                cartAdapter.notifyDataSetChanged();
            });
        }, (cart, quantity) -> {
            // Xử lý thay đổi số lượng
            int index = cartItems.indexOf(cart);

            new Thread(() -> {
                if (index != -1) {
                    cart.setQuantity(quantity); // Cập nhật số lượng
                    cartDAO.update(cart); // Gọi phương thức cập nhật từ DAO
                    calculatePrice(); // Call calculatePrice() after changing quantity
                }
            }).start();
            getActivity().runOnUiThread(() -> cartAdapter.notifyItemChanged(index));
        });

        recyclerViewCart.setAdapter(cartAdapter);
        calculatePrice();
        return view;
    }
    private void calculatePrice() {
        new Thread(() -> {
            int currentUserId = getUserId();
            List<Cart> items = cartDAO.getAllCartItemsByUserId(currentUserId);
            double sum = 0;
            for (Cart cart : items) {
                Log.d("CartFragment", "userID: " + cart.getUserId());
                sum += cart.getQuantity() * cart.getPrice();
            }
            if (items.isEmpty()) {
                sendCartUpdateBroadcast(); // Send broadcast if cart is empty
            }

            // Chuyển cập nhật UI sang luồng chính
            double finalSum = sum; // Tạo biến tạm để sử dụng trong runOnUiThread
            getActivity().runOnUiThread(() -> {
                TextView showItem = getActivity().findViewById(R.id.totalPrice);
                if (showItem != null) {
                    showItem.setText("Total price: " + finalSum + " $");
                }
            });
        }).start();
    }


    private int getUserId() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserIDPrefs", getContext().MODE_PRIVATE);
        return sharedPref.getInt("userID", -1);  // Trả về -1 nếu không tìm thấy userId
    }

    private void sendCartUpdateBroadcast() {
        Intent intent = new Intent("com.example.prm392.CART_UPDATE");
        requireActivity().sendBroadcast(intent);
    }

}