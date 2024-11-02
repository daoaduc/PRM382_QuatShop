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
                    order.setAccID(accountDao.getAccountByID(rs.getInt("accID")));
                    order.setTotalMoney(rs.getDouble("totalMoney"));
                    order.setPaymentMethod(rs.getString("paymentMethod"));
                    order.setOrderDate(rs.getDate("orderDate"));
                    order.setConfirmedDate(rs.getDate("confirmedDate"));
                    order.setPickUpDate(rs.getDate("pickupDate"));
                    order.setDeliveryDate(rs.getDate("deliveryDate"));
                    order.setStatus(this.getOrderStatusById(rs.getInt("status")));
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
                    order.setAccID(accountDao.getAccountByID(rs.getInt("accID")));
                    order.setTotalMoney(rs.getDouble("totalMoney"));
                    order.setPaymentMethod(rs.getString("paymentMethod"));
                    order.setOrderDate(rs.getDate("orderDate"));
                    order.setConfirmedDate(rs.getDate("confirmedDate"));
                    order.setPickUpDate(rs.getDate("pickupDate"));
                    order.setDeliveryDate(rs.getDate("deliveryDate"));
                    order.setStatus(this.getOrderStatusById(rs.getInt("status")));
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

    public OrderStatus getOrderStatusById(int statusID){
        Connection connection = connectionClass.CONN();
        if(connection!=null) {
            String query = "SELECT * FROM order_status WHERE statusID = ?;";
            try{
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, statusID);
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    OrderStatus os = new OrderStatus();
                    os.setStatus(rs.getString("status"));
                    os.setStatusID(rs.getInt(statusID));
                    return os;
                }
            }catch (SQLException e) {
                throw new RuntimeException(e);
            } {

            }
        }
        return null;
    }

}

