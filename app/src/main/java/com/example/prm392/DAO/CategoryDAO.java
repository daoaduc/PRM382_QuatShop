package com.example.prm392.DAO;

import android.util.Log;
import com.example.prm392.ConnectionClass;
import com.example.prm392.model.ProductCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    ConnectionClass connectionClass;

    public CategoryDAO() {
        connectionClass = new ConnectionClass();
    }

    // Phương thức để lấy danh sách sản phẩm từ MySQL
    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> productList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            // Thêm cột 'productIMG' để lấy tên hình ảnh
            String query = "SELECT categoryID, categoryName FROM product_category";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setCategoryID(rs.getInt("categoryID"));
                    productCategory.setCategoryName(rs.getString("categoryName"));

                    productList.add(productCategory);
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

}