package com.example.prm392.model;
public class ProductCategory {
    private int categoryID;
    private String categoryName;
    private String categoryIMG;

    public ProductCategory(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    public ProductCategory(int categoryID, String categoryName, String categoryIMG) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryIMG = categoryIMG;
    }

    public ProductCategory() {

    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIMG() {
        return categoryIMG;
    }

    public void setCategoryIMG(String categoryIMG) {
        this.categoryIMG = categoryIMG;
    }

    @Override
    public String toString() {
        return categoryName; // Hiển thị tên trong Spinner
    }


}
