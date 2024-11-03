package com.example.prm392.DAO;

import android.util.Log;

import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Account;
import com.example.prm392.model.Order;
import com.example.prm392.model.OrderStatus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    ConnectionClass connectionClass;
    AccountDAO accountDao = new AccountDAO();

    public OrderDAO() {
        connectionClass = new ConnectionClass();
    }
    public List<Order> getAllOrders() {
        Log.d("OrderDAO", "getAllOrders() called");
        List<Order> orderList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "select * from `order`;";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderID(rs.getInt("orderID"));
                    order.setOrderCode(rs.getString("orderCode"));
                    order.setAccID(new Account(rs.getInt("accID")));
                    order.setTotalMoney(rs.getLong("totalMoney"));
                    order.setPaymentMethod(rs.getBoolean("paymentMethod"));
                    order.setOrderDate(rs.getDate("orderDate"));
                    order.setConfirmedDate(rs.getDate("confirmedDate"));
                    order.setPickedUpDate(rs.getDate("pickupDate"));
                    order.setDeliveryDate(rs.getDate("deliveryDate"));
                    String status = this.getStatusById(rs.getInt("status"));
                    order.setStatus(new OrderStatus(rs.getInt("status"), status));
                    orderList.add(order);
                    count++;
                }
                Log.e("OrderDAO", "Number of orders: " + count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return orderList;
    }

    public List<Order> getOrderByAccount(int accountID){
        Log.d("OrderDAO", "getOrderByAccount() called");
        Connection connection = connectionClass.CONN();
        List<Order> orderList = new ArrayList<>();
        if(connection!=null){
            String query = "select * from `order` where accID = ?";
            try{
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, accountID);
                ResultSet rs = st.executeQuery();
                int count = 0;
                while(rs.next()){
                    Order order = new Order();
                    order.setOrderID(rs.getInt("orderID"));
                    order.setOrderCode(rs.getString("orderCode"));
                    order.setAccID(new Account(rs.getInt("accID")));
                    order.setTotalMoney(rs.getLong("totalMoney"));
                    order.setPaymentMethod(rs.getBoolean("paymentMethod"));
                    order.setOrderDate(rs.getDate("orderDate"));
                    order.setConfirmedDate(rs.getDate("confirmedDate"));
                    order.setPickedUpDate(rs.getDate("pickupDate"));
                    order.setDeliveryDate(rs.getDate("deliveryDate"));
                    String status = this.getStatusById(rs.getInt("status"));
                    order.setStatus(new OrderStatus(rs.getInt("status"), status));
                    orderList.add(order);
                    count++;
                }
                Log.e("OrderDAO", "Number of orders: " + count);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return orderList;
    }

    public String getStatusById(int statusID){
        Connection connection = connectionClass.CONN();
        if(connection!=null) {
            String query = "SELECT status FROM order_status WHERE statusID = ?;";
            try{
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, statusID);
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    return rs.getString(1);
                }
            }catch (SQLException e) {
                throw new RuntimeException(e);
            } {

            }
        }
        return null;
    }

}

