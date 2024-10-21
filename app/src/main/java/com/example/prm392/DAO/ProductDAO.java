package com.example.prm392.DAO;

import android.util.Log;
import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Product;

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

    public Product getProductById(int productID){
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            // Thêm cột 'productIMG' để lấy tên hình ảnh
            String query = "SELECT productName, price, quantity, description, productIMG FROM products WHERE productID = ?";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, productID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Product product = new Product();
                    product.setProductName(rs.getString("productName"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setPrice(rs.getLong("price"));
                    product.setDescription(rs.getString("description"));

                    // Lấy đường dẫn hình ảnh từ cột 'productIMG'
                    product.setProductIMG(rs.getString("productIMG"));
                    return product;
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
        return null;
    }

}