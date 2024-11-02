package com.example.prm392.DAO;

import android.util.Log;

import com.example.prm392.ConnectionClass;
import com.example.prm392.model.ProductStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductStatusDAO {
    ConnectionClass connectionClass;

    public ProductStatusDAO() {
        connectionClass = new ConnectionClass();
    }

    // Phương thức để lấy danh sách sản phẩm từ MySQL
    public List<ProductStatus> getAllProductStatus() {
        List<ProductStatus> productList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            // Thêm cột 'productIMG' để lấy tên hình ảnh
            String query = "SELECT statusID, statusName FROM product_status";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ProductStatus productStatus = new ProductStatus();
                    productStatus.setStatusID(rs.getInt("statusID"));
                    productStatus.setStatusName(rs.getString("statusName"));

                    productList.add(productStatus);
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
