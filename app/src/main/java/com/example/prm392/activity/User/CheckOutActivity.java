package com.example.prm392.activity.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Api.CreateOrder;
import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.DAO.CartDAO;
import com.example.prm392.DAO.CartDatabase;
import com.example.prm392.DAO.OrderDAO;
import com.example.prm392.DAO.PromoDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.CheckoutListAdapter;
import com.example.prm392.model.Account;
import com.example.prm392.model.Cart;
import com.example.prm392.model.Order;
import com.example.prm392.model.OrderStatus;
import com.example.prm392.model.Promo;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private TextView txtTongTien, txtName, txtAddress, txtPhone;
    private EditText etPromoCode;
    private Button btnPlaceOrder, btnApplyPromocode;
    private RadioGroup paymentMethodGroup;
    private CartDAO cartDAO;
    private double discountAmount = 0;
    private String totalString;
    private int promoId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checkout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();

        });

        cartItems = new ArrayList<>();
        cartDAO = CartDatabase.getInstance(this).cartDAO();

        int userId = getUserId();
        Log.d("CheckOutActivity", "userId: " + userId);

        // Khởi tạo ExecutorService trước khi lấy thông tin tài khoản
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            // Trong luồng này, lấy thông tin tài khoản
            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.getAccountById(userId);
            Log.d("CheckOutActivity", "Account: " + account);
            txtName.setText(account.getFullname());
            String phone = account.getPhoneNumber();
            if (phone != null) {
                txtPhone.setText(phone);
            }


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
        txtName = findViewById(R.id.et_name);
        txtAddress = findViewById(R.id.et_address);
        txtPhone = findViewById(R.id.et_phone);


        // Khởi tạo Promocode

        etPromoCode = findViewById(R.id.et_promocode);
        btnApplyPromocode = findViewById(R.id.btn_apply_promocode);

        btnApplyPromocode.setOnClickListener(v -> {
            String promoCode = etPromoCode.getText().toString().trim();
            applyPromoCode(promoCode);
        });
        // Khởi tạo giao diện thanh toán

        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        // Thiết lập StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Khởi tạo ZaloPay SDK
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        btnPlaceOrder.setOnClickListener(v -> {
            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
            String phoneInput = txtPhone.getText().toString().trim();
            String address = txtAddress.getText().toString().trim();

            // Kiểm tra số điện thoại và địa chỉ
            if (phoneInput.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }
            if (address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi phương thức thanh toán tùy theo phương thức được chọn
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
                        String orderCode = generateOrderCode();
                        int userId = getUserId();
                        String address = txtAddress.getText().toString().trim();
                        String phoneNumber = txtPhone.getText().toString().trim();
                        String totalMoney = totalString;

                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(() -> {
                            Order order = new Order();
                            order.setOrderCode(orderCode);
                            order.setAccID(new Account(userId));
                            order.setAddress(address);
                            order.setTotalMoney(Long.parseLong(totalMoney));
                            order.setPaymentMethod(true);
                            order.setOrderDate(new Date(System.currentTimeMillis()));
                            order.setConfirmedDate(new Date(System.currentTimeMillis()));
                            order.setStatus(new OrderStatus(2));
                            order.setPhone_number(phoneNumber);
                            if (promoId != -1) { // Check if a promo is applied
                                order.setPromoID(new Promo(promoId));
                                PromoDAO promoDAO = new PromoDAO();
                                promoDAO.incrementUsageCount(promoId);
                            }else{
                                order.setPromoID(null);
                            }

                            OrderDAO orderDAO = new OrderDAO();
                            int orderId = orderDAO.insertOrderCard(order); // Lấy orderID tự động sinh

                            if (orderId != 0) { // Nếu chèn thành công
                                for (Cart cart : cartItems) {
                                    int productID = cart.getProductId();
                                    int quantity = cart.getQuantity();
                                    double totalPrice = quantity * cart.getPrice();
                                    long totalPriceLong = (long) totalPrice;
                                    orderDAO.insertOrderDetail(orderId, productID, quantity, totalPriceLong);
                                }
                                cartDAO.deleteAllCartItemsByUserId(userId);
                                runOnUiThread(() -> {
                                    navigateToPaymentNotification("Thanh toán thành công","ĐƠN HÀNG CỦA BẠN ĐÃ ĐƯỢC THANH TOÁN THÀNH CÔNG");
                                });
                            } else {
                                Log.e("ERROR", "Order insertion failed, no orderID generated");
                            }

                            executorService.shutdown();});
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Intent intent = new Intent(CheckOutActivity.this, MainActivity2.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        navigateToPaymentNotification("Lỗi thanh toán","GẶP LỖI KHI THỰC HIỆN THANH TOÁN ĐƠN HÀNG");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCashPayment() {
        String orderCode = generateOrderCode();
        int userId = getUserId();
        String address = txtAddress.getText().toString().trim();
        String phoneNumber = txtPhone.getText().toString().trim();
        String totalMoney = totalString;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Order order = new Order();
            order.setOrderCode(orderCode);
            order.setAccID(new Account(userId));
            order.setAddress(address);
            order.setTotalMoney(Long.parseLong(totalMoney));
            order.setPaymentMethod(false);
            order.setOrderDate(new Date(System.currentTimeMillis()));
            order.setConfirmedDate(null);
            order.setStatus(new OrderStatus(1));
            order.setPhone_number(phoneNumber);
            if (promoId != -1) {
                order.setPromoID(new Promo(promoId));
                PromoDAO promoDAO = new PromoDAO();
                promoDAO.incrementUsageCount(promoId);
            }else{
                order.setPromoID(null);
            }

            OrderDAO orderDAO = new OrderDAO();
            int orderId = orderDAO.insertOrder(order); // Lấy orderID tự động sinh

            if (orderId != 0) { // Nếu chèn thành công
                for (Cart cart : cartItems) {
                    int productID = cart.getProductId();
                    int quantity = cart.getQuantity();
                    double totalPrice = quantity * cart.getPrice();
                    long totalPriceLong = (long) totalPrice;
                    orderDAO.insertOrderDetail(orderId, productID, quantity, totalPriceLong);
                }
                cartDAO.deleteAllCartItemsByUserId(userId);
                runOnUiThread(() -> {
                    navigateToPaymentNotification("Đang chờ thanh toán", "Cùng Fan Shop bảo vệ quyền lợi của bạn - KHÔNG CHUYỂN TIỀN TRƯỚC cho Shipper khi đơn hàng chưa được giao tới với bất kì lý do gì");
                });
            } else {
                Log.e("ERROR", "Order insertion failed, no orderID generated");
            }
        });
    }

    private String generateOrderCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder orderCode = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * characters.length());
            orderCode.append(characters.charAt(index));
        }
        return orderCode.toString();
    }

    private void navigateToPaymentNotification(String textViewNotify, String textViewMessage) {
        Intent intent = new Intent(CheckOutActivity.this, PaymentNotification.class);
        intent.putExtra("textViewNotify", textViewNotify);
        intent.putExtra("textViewMessage", textViewMessage);
        startActivity(intent);
    }

    private void applyPromoCode(String promoCode) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            PromoDAO promoDAO = new PromoDAO();
            Promo promo = promoDAO.getPromoByPromoCode(promoCode);

            // Kiểm tra mã khuyến mãi
            if (promo != null && promoCode.equalsIgnoreCase(promo.getPromoCode())) {
                discountAmount = promo.getPromoValue();
                promoId = promo.getPromoID();
                runOnUiThread(() -> {
                    // Hiển thị thông báo và tính lại giá
                    if (promo.isPromoType()) {
                        Toast.makeText(this, "Mã giảm giá hợp lệ! Giảm " + discountAmount + "%", Toast.LENGTH_SHORT).show();
                        discountAmount = discountAmount / 100;
                    } else {
                        if (discountAmount < Double.parseDouble(totalString)) {
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                            String discountAmountString = String.format("%.0f", discountAmount);
                            Toast.makeText(this, "Mã giảm giá hợp lệ! Giảm " + discountAmountString + " VND", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Không thể áp dụng mã giảm giá này", Toast.LENGTH_SHORT).show();
                        }
                    }
                    calculatePrice();
                });
            } else {
                discountAmount = 0;
                promoId = -1;
                runOnUiThread(() -> {
                    Toast.makeText(this, "Mã giảm giá không hợp lệ hoặc đã hết giá trị sử dụng!", Toast.LENGTH_SHORT).show();
                    calculatePrice(); // Tính toán lại giá để đảm bảo
                });
            }
            executorService.shutdown();
        });
    }

    private void calculatePrice() {
        new Thread(() -> {
            int currentUserId = getUserId();
            List<Cart> items = cartDAO.getAllCartItemsByUserId(currentUserId);
            double sum = 0;
            for (Cart cart : items) {
                sum += cart.getQuantity() * cart.getPrice();
            }

            // Áp dụng số tiền giảm giá một cách chính xác
            double finalAmount;
            if (discountAmount < 1) {
                finalAmount = sum * (1 - discountAmount);
            } else {
                finalAmount = sum - discountAmount;
                if (finalAmount < 0) {
                    finalAmount = 0;
                }
            }

            // Định dạng tổng tiền sau khi giảm giá
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            totalString = String.format("%.0f", finalAmount);
            String formattedPrice = formatter.format(finalAmount) + " VNĐ";

            // Cập nhật TextView trên giao diện chính
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