package com.example.prm392.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class Cart {
    @PrimaryKey(autoGenerate = true)
    private int CartId;

    private int ProductId;
    private String ProductName;
    private double price;
    private int quantity;
    private String image;

    public Cart() {
    }

    public Cart(int cartId, int productId, String productName, double price, int quantity, String image) {
        CartId = cartId;
        ProductId = productId;
        ProductName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public int getCartId() {
        return CartId;
    }

    public void setCartId(int cartId) {
        CartId = cartId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
