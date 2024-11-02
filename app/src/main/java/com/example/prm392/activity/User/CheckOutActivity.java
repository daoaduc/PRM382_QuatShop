//package com.example.prm392.activity.User;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.prm392.Api.CreateOrder;
//import com.example.prm392.DAO.AccountDAO;
//import com.example.prm392.DAO.CartDAO;
//import com.example.prm392.DAO.CartDatabase;
//import com.example.prm392.DAO.OrderDAO;
//import com.example.prm392.R;
//import com.example.prm392.adapter.CheckoutListAdapter;
//import com.example.prm392.model.Account;
//import com.example.prm392.model.Cart;
//import com.example.prm392.model.Order;
//import com.example.prm392.model.OrderStatus;
//
//import org.json.JSONObject;
//
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import vn.zalopay.sdk.Environment;
//import vn.zalopay.sdk.ZaloPayError;
//import vn.zalopay.sdk.ZaloPaySDK;
//import vn.zalopay.sdk.listeners.PayOrderListener;
//
//public class CheckOutActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private CheckoutListAdapter adapter;
//    private List<Cart> cartItems; // Danh sách chứa sản phẩm trong giỏ hàng
//    private TextView txtTongTien, txtName, txtAddress, txtPhone;
//    private Button btnPlaceOrder;
//    private RadioGroup paymentMethodGroup;
//    private CartDAO cartDAO;
//
//    private String totalString;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_checkout);
//
//        cartItems = new ArrayList<>();
//        cartDAO = CartDatabase.getInstance(this).cartDAO();
//
//        int userId = getUserId();
//        Log.d("CheckOutActivity", "userId: " + userId);
//
//        // Khởi tạo ExecutorService trước khi lấy thông tin tài khoản
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
//            // Trong luồng này, lấy thông tin tài khoản
//            AccountDAO accountDAO = new AccountDAO();
//            Account account = accountDAO.getAccountById(userId);
//            Log.d("CheckOutActivity", "Account: " + account);
//            txtName.setText(account.getFullname());
//            String phone = account.getPhoneNumber();
//            if (phone != null) {
//                txtPhone.setText(phone);
//            }
//
//
//            cartItems.addAll(cartDAO.getAllCartItemsByUserId(userId));
//            Log.d("CheckOutActivity", "cartItems size: " + cartItems.size());
//
//            runOnUiThread(() -> {
//                adapter = new CheckoutListAdapter(this, cartItems);
//                recyclerView.setAdapter(adapter);
//                calculatePrice();
//            });
//        });
//
//        // Khởi tạo RecyclerView
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        btnPlaceOrder = findViewById(R.id.btn_place_order);
//        txtTongTien = findViewById(R.id.tv_total_price);
//        txtName = findViewById(R.id.et_name);
//        txtAddress = findViewById(R.id.et_address);
//        txtPhone = findViewById(R.id.et_phone);
//
//
//        // Khởi tạo giao diện thanh toán
//
//        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
//
//        // Thiết lập StrictMode
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        // Khởi tạo ZaloPay SDK
//        ZaloPaySDK.init(2553, Environment.SANDBOX);
//
//        btnPlaceOrder.setOnClickListener(v -> {
//            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
//            String phoneinput = txtPhone.getText().toString().trim();
//            String address = txtAddress.getText().toString().trim();
//            if (phoneinput.isEmpty()) {
//                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (address.isEmpty()) {
//                Toast.makeText(this, "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (selectedId == R.id.radioZaloPay) {
//                handleZaloPay(totalString);
//            } else if (selectedId == R.id.radioCash) {
//                handleCashPayment();
//            } else {
//                Toast.makeText(CheckOutActivity.this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    private void handleZaloPay(String totalString) {
//        CreateOrder orderApi = new CreateOrder();
//        try {
//            JSONObject data = orderApi.createOrder(totalString);
//            String code = data.getString("return_code");
//            if (code.equals("1")) {
//                String token = data.getString("zp_trans_token");
//                ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
//                    @Override
//                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                        navigateToPaymentNotification("Thanh toán thành công");
//
//                    }
//
//                    @Override
//                    public void onPaymentCanceled(String s, String s1) {
//                        navigateToPaymentNotification("Hủy thanh toán");
//                    }
//
//                    @Override
//                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//                        navigateToPaymentNotification("Lỗi thanh toán");
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleCashPayment() {
//        String orderCode = generateOrderCode();
//        int userId = getUserId();
//        String address = txtAddress.getText().toString().trim();
//        String phoneNumber = txtPhone.getText().toString().trim();
//        String totalMoney = totalString;
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
//            // Tạo đối tượng Order với các thông tin cần thiết
//            Order order = new Order();
//            order.setOrderCode(orderCode);
//            order.setAccID(new Account(userId));  // Giả sử hàm getUserId trả về accID
//            order.setAddress(address);
//            order.setTotalMoney(Long.parseLong(totalMoney));
//            order.setPaymentMethod(false); // Giả sử false là thanh toán tiền mặt
//            order.setOrderDate(new Date(System.currentTimeMillis()));
//            order.setConfirmedDate(null);  // Có thể để null nếu chưa xác nhận
//            order.setStatus(new OrderStatus(1)); // Trạng thái mặc định
//            order.setPhone_number(phoneNumber);
//
//            // Gọi phương thức insertOrder để chèn dữ liệu vào database
//            OrderDAO orderDAO = new OrderDAO();
//            orderDAO.insertOrder(order);
//
//            cartDAO.deleteAllCartItemsByUserId(userId);
//
//            runOnUiThread(() -> {
//                navigateToPaymentNotification("Thanh toán bằng tiền mặt");
//            });
//        });
//    }
//
//
//    private String generateOrderCode() {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder orderCode = new StringBuilder(10);
//        for (int i = 0; i < 10; i++) {
//            int index = (int) (Math.random() * characters.length());
//            orderCode.append(characters.charAt(index));
//        }
//        return orderCode.toString();
//    }
//
//    private void navigateToPaymentNotification(String result) {
//        Intent intent = new Intent(CheckOutActivity.this, PaymentNotification.class);
//        intent.putExtra("result", result);
//        startActivity(intent);
//    }
//
//    private void calculatePrice() {
//        new Thread(() -> {
//            int currentUserId = getUserId();
//            List<Cart> items = cartDAO.getAllCartItemsByUserId(currentUserId);
//            double sum = 0;
//            for (Cart cart : items) {
//                sum += cart.getQuantity() * cart.getPrice();
//            }
//
//            // Format tổng tiền
//            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
//            totalString = String.format("%.0f", sum); // Cập nhật totalString với tổng tiền
//            String formattedPrice = formatter.format(sum) + " VNĐ";
//
//            // Cập nhật TextView trên main thread
//            runOnUiThread(() -> txtTongTien.setText("Tổng tiền: " + formattedPrice));
//        }).start();
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        ZaloPaySDK.getInstance().onResult(intent);
//    }
//
//    private Integer getUserId() {
//        SharedPreferences sharedPref = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
//        return sharedPref.contains("userID") ? sharedPref.getInt("userID", -1) : null;
//    }
//}
