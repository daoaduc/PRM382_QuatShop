package com.example.prm392.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Api.CreateOrder;
import com.example.prm392.R;
import com.example.prm392.adapter.CheckoutListAdapter;
import com.example.prm392.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CheckoutListAdapter adapter;
    private List<Product> dataList; // Danh sách chứa tất cả sản phẩm
    TextView txtTongTien;
    Button btnPlaceOrder;
    RadioGroup paymentMethodGroup ; // Thêm RadioGroup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checkout);

        btnPlaceOrder = findViewById(R.id.btn_place_order);
        txtTongTien = findViewById(R.id.tv_total_price);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        Double total = (double) 1000000;
        String totalString = String.format("%.0f", total);
        txtTongTien.setText(Double.toString(total));

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.radioZaloPay) { // Nếu chọn ZaloPay
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        JSONObject data = orderApi.createOrder(totalString);
                        String code = data.getString("return_code");
                        if (code.equals("1")) {
                            String token = data.getString("zp_trans_token");
                            ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(String s, String s1, String s2) {
                                    Intent intent1 = new Intent(CheckOutActivity.this, PaymentNotification.class);
                                    intent1.putExtra("result", "Thanh toán thành công");
                                    startActivity(intent1);
                                }

                                @Override
                                public void onPaymentCanceled(String s, String s1) {
                                    Intent intent1 = new Intent(CheckOutActivity.this, PaymentNotification.class);
                                    intent1.putExtra("result", "Hủy thanh toán");
                                    startActivity(intent1);
                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                    Intent intent1 = new Intent(CheckOutActivity.this, PaymentNotification.class);
                                    intent1.putExtra("result", "Lỗi thanh toán");
                                    startActivity(intent1);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (selectedId == R.id.radioCash) { // Nếu chọn tiền mặt
                    // Xử lý thanh toán bằng tiền mặt ở đây
                    Intent intent1 = new Intent(CheckOutActivity.this, PaymentNotification.class);
                    intent1.putExtra("result", "Thanh toán bằng tiền mặt");
                    startActivity(intent1);
                } else {
                    // Nếu chưa chọn phương thức thanh toán
                    // Thông báo cho người dùng
                    Toast.makeText(CheckOutActivity.this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
