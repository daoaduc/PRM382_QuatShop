package com.example.prm392.model;


import java.sql.Date;

public class Order {
    private int orderID;
    private String orderCode;
    private Account accID;
    private double totalMoney;
    private String paymentMethod;
    private Date orderDate;
    private Date confirmedDate;
    private Date pickUpDate;
    private Date deliveryDate;
    private OrderStatus status;

    public Order() {
    }

    public Order(int orderID, String orderCode, Account accID, double totalMoney, String paymentMethod, Date orderDate, Date confirmedDate, Date pickUpDate, Date deliveryDate, OrderStatus status) {
        this.orderID = orderID;
        this.orderCode = orderCode;
        this.accID = accID;
        this.totalMoney = totalMoney;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.confirmedDate = confirmedDate;
        this.pickUpDate = pickUpDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Account getAccID() {
        return accID;
    }

    public void setAccID(Account accID) {
        this.accID = accID;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
