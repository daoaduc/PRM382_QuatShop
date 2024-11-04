package com.example.prm392.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.R;
import com.example.prm392.adapter.CartAdapter;
import com.example.prm392.model.Cart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.util.Log;
import android.widget.Toast;

public class CartFragment extends Fragment {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private CartDAO cartDAO;
    private Button checkoutButton;
    List<Cart> cartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerViewCart = view.findViewById(R.id.cartRecycleView);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        checkoutButton = view.findViewById(R.id.button_CheckOut);
        // Initialize cartItems list
        cartItems = new ArrayList<>();
        cartDAO = CartDatabase.getInstance(getContext()).cartDAO();
        new Thread(() -> {
            int userId = getUserId();
            Log.d("CartFragment", "userId: " + userId);
            if (userId != -1) { // Ensure valid userId
                cartItems.addAll(cartDAO.getAllCartItemsByUserId(userId));
                getActivity().runOnUiThread(() -> cartAdapter.notifyDataSetChanged());
            }
        }).start();

        // Initialize adapter with listeners for item delete and quantity change
        cartAdapter = new CartAdapter(cartItems, cart -> {
            new Thread(() -> {
                cartDAO.delete(cart);
                getActivity().runOnUiThread(() -> {
                    cartItems.remove(cart);
                    cartAdapter.notifyDataSetChanged();
                    calculatePrice();
                        sendCartUpdateBroadcast();

                });
            }).start();
        }, (cart, quantity) -> {
            int index = cartItems.indexOf(cart);
            new Thread(() -> {
                if (index != -1) {
                    cart.setQuantity(quantity);
                    cartDAO.update(cart);
                    getActivity().runOnUiThread(() -> {
                        cartAdapter.notifyItemChanged(index);
                        calculatePrice();
                    });
                }
            }).start();
        });

        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                // Hiển thị Toast nếu giỏ hàng rỗng
                Toast.makeText(getContext(), "Giỏ hàng rỗng! Vui lòng thêm sản phẩm.", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu giỏ hàng không rỗng, chuyển đến CheckoutActivity
                Intent intent = new Intent(getContext(), CheckOutActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewCart.setAdapter(cartAdapter);
        calculatePrice();
        return view;
    }

    private void calculatePrice() {
        new Thread(() -> {
            int currentUserId = getUserId();
            if (currentUserId != -1) {
                List<Cart> items = cartDAO.getAllCartItemsByUserId(currentUserId);
                double sum = 0;
                for (Cart cart : items) {
                    Log.d("CartFragment", "userID: " + cart.getUserId());
                    sum += cart.getQuantity() * cart.getPrice();
                }
                    sendCartUpdateBroadcast();


                double finalSum = sum;
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                        String formattedPrice = formatter.format(finalSum) + " VNĐ";
                        TextView showItem = getActivity().findViewById(R.id.totalPrice);
                        if (showItem != null) {
                            showItem.setText("Total price: " + formattedPrice );
                        }
                    });
                }
            }
        }).start();
    }

    private int getUserId() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserIDPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userID", -1);
        if (userId == -1) {
            Log.e("CartFragment", "User ID not found in SharedPreferences");
        }
        return userId;
    }

    private void sendCartUpdateBroadcast() {
        Intent intent = new Intent("com.example.prm392.CART_UPDATE");
        if (getActivity() != null) {
            getActivity().sendBroadcast(intent);
        }
    }
}
