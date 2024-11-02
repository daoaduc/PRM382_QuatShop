package com.example.prm392.DAO;

import android.util.Log;

import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Order;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class OrderDAO {
    ConnectionClass connectionClass;

    public OrderDAO() {
        connectionClass = new ConnectionClass();
    }
    public void insertOrder(Order order) {
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            // Câu lệnh SQL để chèn sản phẩm mới vào bảng
            String query = "INSERT INTO `order` (orderCode, accID, address, totalMoney, paymentMethod, orderDate, confirmedDate,status,phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, order.getOrderCode());
                stmt.setInt(2, order.getAccID().getAccID());
                stmt.setString(3, order.getAddress());
                stmt.setLong(4, order.getTotalMoney());
                stmt.setBoolean(5, order.isPaymentMethod());

                // Lấy ngày hiện tại theo định dạng yyyy-MM-dd
                Date orderDate = new Date(System.currentTimeMillis());
                stmt.setDate(6, orderDate);
                stmt.setDate(7, (Date) order.getConfirmedDate());
                stmt.setInt(8, order.getStatus().getStatusID());
                stmt.setString(9, order.getPhone_number());


                // Thực hiện chèn dữ liệu
                stmt.executeUpdate();

                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while inserting order: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }
    }
}
