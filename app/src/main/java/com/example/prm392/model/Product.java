package com.example.prm392.model;

import java.util.Date;

public class Product {
    private int productID;
    private String productName;
    private  long price;
    private long discount;
    private int quantity;
    private String description;
    private ProductCategory categoryID;
    private int sold;
    private ProductStatus status;
    private String productIMG;
    private Date creat_at;

    public Product() {
    }

    public Product(String productName, String productIMG) {
        this.productID = productID;
        this.productName = productName;
        this.productIMG = productIMG;
    }

    public Product(String productName, long price, String productIMG) {
        this.productName = productName;
        this.price = price;
        this.productIMG = productIMG;
    }

    public Product(int productID, String productName, long price, long discount, int quantity, String description, ProductCategory categoryID, int sold, ProductStatus status, String productIMG, Date creat_at) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.description = description;
        this.categoryID = categoryID;
        this.sold = sold;
        this.status = status;
        this.productIMG = productIMG;
        this.creat_at = creat_at;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(ProductCategory categoryID) {
        this.categoryID = categoryID;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getProductIMG() {
        return productIMG;
    }

    public void setProductIMG(String productIMG) {
        this.productIMG = productIMG;
    }

    public Date getCreat_at() {
        return creat_at;
    }

    public void setCreat_at(Date creat_at) {
        this.creat_at = creat_at;
    }
}
