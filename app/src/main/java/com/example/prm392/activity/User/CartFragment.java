package com.example.prm392.activity.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
            for(Cart item : cartDAO.getAllCartItems())
            {
                cartItems.add(item);
            }}).start();

        // Khởi tạo adapter với cả hai listener
        cartAdapter = new CartAdapter(cartItems, cart -> {
            new Thread(() -> cartDAO.delete(cart)).start();
            cartItems.remove(cart);
            getActivity().runOnUiThread(() -> cartAdapter.notifyDataSetChanged());

        }, (cart, quantity) -> {
            // Xử lý thay đổi số lượng
            int index = cartItems.indexOf(cart);

            new Thread(() -> {
                if (index != -1) {
                    cart.setQuantity(quantity); // Cập nhật số lượng
                    cartDAO.update(cart); // Gọi phương thức cập nhật từ DAO
                    calculatePrice();

                }
            }).start();
            getActivity().runOnUiThread(() -> cartAdapter.notifyItemChanged(index)); });

        recyclerViewCart.setAdapter(cartAdapter);
        calculatePrice();
        return view;
    }
    private void calculatePrice()
    {
        new Thread(() -> {
            List<Cart> items = cartDAO.getAllCartItems();
            double sum = 0;
            for(Cart  cart : items)
            {
                Log.d("CartFragment", "userID: " + cart.getUserId()); // Add this line
                sum+= cart.getQuantity() * cart.getPrice();
            }
            TextView showItem = this.getActivity().findViewById(R.id.totalPrice);
            if(showItem
                    != null)
            {
                showItem.setText("Total price: "+sum+" $");
            }
        }).start();
    }
}