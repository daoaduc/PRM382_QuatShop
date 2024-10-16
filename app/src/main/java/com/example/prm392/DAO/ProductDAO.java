package com.example.prm392.DAO;

import android.util.Log;

import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    ConnectionClass connectionClass;

    public ProductDAO() {
        connectionClass = new ConnectionClass();
    }

    // Phương thức để lấy danh sách sản phẩm từ MySQL
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            // Thêm cột 'productIMG' để lấy tên hình ảnh
            String query = "SELECT productName, quantity, productIMG FROM products";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Product product = new Product();
                    product.setProductName(rs.getString("productName"));
                    product.setQuantity(rs.getInt("quantity"));

                    // Lấy đường dẫn hình ảnh từ cột 'productIMG'
                    product.setProductIMG(rs.getString("productIMG"));

                    productList.add(product);
                }

                rs.close();
                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while fetching products: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }

        return productList;
    }

    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> categoriesList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "SELECT categoryID, categoryName, categoryIMG FROM product_category";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ProductCategory category = new ProductCategory(
                            rs.getInt("categoryID"),
                            rs.getString("categoryName"),
                            rs.getString("categoryIMG")
                    );
                    categoriesList.add(category);
                    Log.d("SQL_QUERY", "Executing query: " + query);
                    Log.d("SQL_RESULT", "Number of rows returned: " + categoriesList.size());
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error fetching categories: " + e.getMessage());
            }
        }
        return categoriesList;
    }


    public List<Product> getTop3BestSellers() {
        List<Product> bestSellers = new ArrayList<>();
        Connection connection = connectionClass.CONN();
        if (connection != null) {
            String query = "SELECT productName, price, productIMG FROM products ORDER BY sold DESC LIMIT 3";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductName(rs.getString("productName"));
                    product.setPrice(rs.getLong("price"));
                    product.setProductIMG(rs.getString("productIMG"));
                    bestSellers.add(product);
                }
                rs.close();
                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error fetching best sellers: " + e.getMessage());
            }
        }
        return bestSellers;
    }


    public List<Product> getAllProducts2() {
        List<Product> productList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "SELECT productID, productName, price, productIMG FROM products";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setProductName(rs.getString("productName"));
                    product.setPrice(rs.getLong("price"));
                    product.setProductIMG(rs.getString("productIMG"));
                    productList.add(product);
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error fetching products: " + e.getMessage());
            }
        }
        return productList;
    }

    public List<Product> getProductsBySearching() {
        List<Product> productList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "SELECT productID, productName, price, productIMG FROM products";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setProductName(rs.getString("productName"));
                    product.setPrice(rs.getLong("price"));
                    product.setProductIMG(rs.getString("productIMG"));
                    productList.add(product);
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error fetching products: " + e.getMessage());
            }
        }
        return productList;
    }
}