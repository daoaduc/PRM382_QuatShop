package com.example.prm392.model;

import java.util.Date;

public class Order {
    private int orderID;
    private String orderCode;
    private Account accID;
    private String address;
    private String phone_number;
    private long totalMoney;
    private boolean paymentMethod;
    private Date orderDate;
    private Date confirmedDate;
    private Date pickedUpDate;
    private Date deliveryDate;
    private OrderStatus status;

    public Order() {
    }

    public Order(int orderID, String orderCode, Account accID, String address, String phone_number, long totalMoney, boolean paymentMethod, Date orderDate, Date confirmedDate, Date pickedUpDate, Date deliveryDate, OrderStatus status) {
        this.orderID = orderID;
        this.orderCode = orderCode;
        this.accID = accID;
        this.address = address;
        this.phone_number = phone_number;
        this.totalMoney = totalMoney;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.confirmedDate = confirmedDate;
        this.pickedUpDate = pickedUpDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public boolean isPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(boolean paymentMethod) {
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

    public Date getPickedUpDate() {
        return pickedUpDate;
    }

    public void setPickedUpDate(Date pickedUpDate) {
        this.pickedUpDate = pickedUpDate;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", orderCode='" + orderCode + '\'' +
                ", accID=" + accID +
                ", address='" + address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", totalMoney=" + totalMoney +
                ", paymentMethod=" + paymentMethod +
                ", orderDate=" + orderDate +
                ", confirmedDate=" + confirmedDate +
                ", pickedUpDate=" + pickedUpDate +
                ", deliveryDate=" + deliveryDate +
                ", status=" + status +
                '}';
    }

}
