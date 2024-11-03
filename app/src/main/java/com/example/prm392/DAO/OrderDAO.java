package com.example.prm392.DAO;

import android.util.Log;

import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Order;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDAO {
    ConnectionClass connectionClass;

    public OrderDAO() {
        connectionClass = new ConnectionClass();
    }
    public int insertOrder(Order order) {
        int generatedOrderID = 0;
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "INSERT INTO `order` (orderCode, accID, address, totalMoney, paymentMethod, orderDate, confirmedDate, status, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, order.getOrderCode());
                stmt.setInt(2, order.getAccID().getAccID());
                stmt.setString(3, order.getAddress());
                stmt.setLong(4, order.getTotalMoney());
                stmt.setBoolean(5, order.isPaymentMethod());
                Date orderDate = new Date(System.currentTimeMillis());
                stmt.setDate(6, orderDate);
                stmt.setDate(7, (Date) order.getConfirmedDate());
                stmt.setInt(8, order.getStatus().getStatusID());
                stmt.setString(9, order.getPhone_number());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedOrderID = rs.getInt(1); // Lấy orderID tự động sinh
                }

                rs.close();
                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while inserting order: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }
        return generatedOrderID;
    }

    public int insertOrderCard(Order order) {
        int generatedOrderID = 0;
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "INSERT INTO `order` (orderCode, accID, address, totalMoney, paymentMethod, orderDate, confirmedDate, status, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, order.getOrderCode());
                stmt.setInt(2, order.getAccID().getAccID());
                stmt.setString(3, order.getAddress());
                stmt.setLong(4, order.getTotalMoney());
                stmt.setBoolean(5, order.isPaymentMethod());
                Date orderDate = new Date(System.currentTimeMillis());
                stmt.setDate(6, orderDate);
                stmt.setDate(7,orderDate);
                stmt.setInt(8, order.getStatus().getStatusID());
                stmt.setString(9, order.getPhone_number());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedOrderID = rs.getInt(1); // Lấy orderID tự động sinh
                }

                rs.close();
                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while inserting order: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }
        return generatedOrderID;
    }


    public void insertOrderDetail(int orderID, int productID, int quantity, long totalPrice) {
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "INSERT INTO order_detail (orderID, productID, quantity, total_price) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, orderID);
                stmt.setInt(2, productID);
                stmt.setInt(3, quantity);
                stmt.setDouble(4, totalPrice);

                stmt.executeUpdate();
                stmt.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while inserting order detail: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    Log.e("ERROR", "Error while closing connection: " + e.getMessage());
                }
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }
    }

    public int getLastOrderID() {
        int lastOrderID = -1; // Giá trị mặc định nếu không tìm thấy
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "SELECT MAX(orderID) as lastID FROM `order`";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    lastOrderID = rs.getInt("lastID");
                }

                rs.close();
                stmt.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while getting last order ID: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    Log.e("ERROR", "Error while closing connection: " + e.getMessage());
                }
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }

        return lastOrderID;
    }


}
