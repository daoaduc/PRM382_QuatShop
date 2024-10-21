package com.example.prm392.DAO;

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
        List<Order> orderList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "SELECT orderID, orderCode, accID, totalMoney, paymentMethod, orderDate, \" +\n" +
                    "                    \"confirmedDate, pickUpDate, deliveryDate, status FROM order";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderID(rs.getInt("orderId"));
                    order.setOrderCode(rs.getString("orderCode"));
                    //order.getAccID(rs.getString("username"));
                    order.setTotalMoney(rs.getDouble("totalMoney"));
                    order.setPaymentMethod(rs.getString("paymentMethod"));
                    order.setOrderDate(rs.getDate("orderDate"));
                    order.setConfirmedDate(rs.getDate("confirmedDate"));
                    order.setPickUpDate(rs.getDate("pickupDate"));
                    order.setDeliveryDate(rs.getDate("deliveryDate"));
                    order.setStatus(this.getOrderStatusById(rs.getInt("status")));

                    orderList.add(order);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return orderList;
    }

    public List<Order> getOrderByAccount(int accountID){
        Connection connection = connectionClass.CONN();
        List<Order> orderList = new ArrayList<>();
        if(connection!=null){
            String query = "SELECT o.orderID, o.orderCode, o.totalMoney, o.paymentMethod, \"\n" +
                    "                   + \"o.orderDate, o.confirmedDate, o.pickUpDate, o.deliveryDate, \"\n" +
                    "                   + \"o.statusID, a.accID \"\n" +
                    "                   + \"FROM orders o JOIN accounts a ON o.accID = a.accID \"\n" +
                    "                   + \"WHERE a.accID = ?";
            try{
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, accountID);
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    int orderID = rs.getInt("orderID");
                    String orderCode = rs.getString("orderCode");
                    double totalMoney = rs.getDouble("totalMoney");
                    String paymentMethod = rs.getString("paymentMethod");
                    Date orderDate = rs.getDate("orderDate");
                    Date confirmedDate = rs.getDate("confirmedDate");
                    Date pickUpDate = rs.getDate("pickUpDate");
                    Date deliveryDate = rs.getDate("deliveryDate");
                    int statusID = rs.getInt("statusID");

                    // Assuming you have a method to get OrderStatus from its ID
                    OrderStatus status = getOrderStatusById(statusID); // Implement this method based on your logic
                    Account accID = accountDao.getAccountByID(rs.getInt("accID")); // Create Account object as needed

                    // Create Order object and add to the list
                    Order order = new Order(orderID, orderCode, accID, totalMoney, paymentMethod, orderDate, confirmedDate, pickUpDate, deliveryDate, status);
                    orderList.add(order);
                }
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

