package com.example.prm392.activity.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Api.CreateOrder;
import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.R;
import com.example.prm392.adapter.CheckoutListAdapter;
import com.example.prm392.model.Account;
import com.example.prm392.model.Cart;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CheckoutListAdapter adapter;
    private List<Cart> cartItems; // Danh sách chứa sản phẩm trong giỏ hàng
    private TextView txtTongTien;
    private Button btnPlaceOrder;
    private RadioGroup paymentMethodGroup;
    private CartDAO cartDAO;

    private String totalString;
    private String name;
    private String address;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checkout);

        cartItems = new ArrayList<>();
        cartDAO = CartDatabase.getInstance(this).cartDAO();

        int userId = getUserId();
        Log.d("CheckOutActivity", "userId: " + userId);

        // Khởi tạo ExecutorService trước khi lấy thông tin tài khoản
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            // Trong luồng này, lấy thông tin tài khoản
            AccountDAO accountDAO = new AccountDAO();
            Account a = accountDAO.getAccountById(userId);
            Log.d("CheckOutActivity", "Account: " + a);

            // Sau khi lấy thông tin tài khoản, tiếp tục lấy danh sách sản phẩm trong giỏ hàng
            cartItems.addAll(cartDAO.getAllCartItemsByUserId(userId));
            Log.d("CheckOutActivity", "cartItems size: " + cartItems.size());

            runOnUiThread(() -> {
                adapter = new CheckoutListAdapter(this, cartItems);
                recyclerView.setAdapter(adapter);
                calculatePrice();
            });
        });

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnPlaceOrder = findViewById(R.id.btn_place_order);
        txtTongTien = findViewById(R.id.tv_total_price);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        // Thiết lập StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Khởi tạo ZaloPay SDK
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        btnPlaceOrder.setOnClickListener(v -> {
            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.radioZaloPay) {
                handleZaloPay(totalString);
            } else if (selectedId == R.id.radioCash) {
                handleCashPayment();
            } else {
                Toast.makeText(CheckOutActivity.this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void handleZaloPay(String totalString) {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(totalString);
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        navigateToPaymentNotification("Thanh toán thành công");
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        navigateToPaymentNotification("Hủy thanh toán");
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        navigateToPaymentNotification("Lỗi thanh toán");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCashPayment() {
        navigateToPaymentNotification("Thanh toán bằng tiền mặt");
    }

    private void navigateToPaymentNotification(String result) {
        Intent intent = new Intent(CheckOutActivity.this, PaymentNotification.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }

    private void calculatePrice() {
        new Thread(() -> {
            int currentUserId = getUserId();
            List<Cart> items = cartDAO.getAllCartItemsByUserId(currentUserId);
            double sum = 0;
            for (Cart cart : items) {
                sum += cart.getQuantity() * cart.getPrice();
            }

            // Format tổng tiền
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            totalString = String.format("%.0f", sum); // Cập nhật totalString với tổng tiền
            String formattedPrice = formatter.format(sum) + " VNĐ";

            // Cập nhật TextView trên main thread
            runOnUiThread(() -> txtTongTien.setText("Tổng tiền: " + formattedPrice));
        }).start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private Integer getUserId() {
        SharedPreferences sharedPref = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        return sharedPref.contains("userID") ? sharedPref.getInt("userID", -1) : null;
    }
}
