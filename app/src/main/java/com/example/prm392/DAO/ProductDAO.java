package com.example.prm392.DAO;

import android.util.Log;
import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class ProductDAO {
    ConnectionClass connectionClass;

    public ProductDAO() {
        connectionClass = new ConnectionClass();
    }

    // Phương thức để lấy danh sách sản phẩm từ MySQL
    public List<Product> getAllAdminProducts() {
        List<Product> productList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "SELECT p.productID, p.productName, p.description, p.quantity, p.price, p.sold, p.productIMG, p.status, ps.statusName " +
                    "FROM products p " +
                    "JOIN product_status ps ON p.status = ps.statusID";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int statusID = rs.getInt("status");
                    String statusName = rs.getString("statusName");
                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setProductName(rs.getString("productName"));
                    product.setDescription(rs.getString("description"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setPrice(rs.getLong("price"));
                    product.setSold(rs.getInt("sold"));
                    product.setStatus(new ProductStatus(statusID,statusName));

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


    public void insertProduct(Product product) {
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            // Câu lệnh SQL để chèn sản phẩm mới vào bảng
            String query = "INSERT INTO products (productName, quantity, description, categoryID, sold, status, productIMG, create_at, price, discount) VALUES (?, ?, ?, ?, 0, 1, ?, ?, ?, 0)";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, product.getProductName());
                stmt.setInt(2, product.getQuantity());
                stmt.setString(3, product.getDescription());
                stmt.setInt(4, product.getCategoryID().getCategoryID());
                stmt.setString(5, product.getProductIMG());

                // Lấy ngày hiện tại theo định dạng yyyy-MM-dd
                Date currentDate = new Date(System.currentTimeMillis());
                stmt.setDate(6, currentDate);
                stmt.setLong(7, product.getPrice());


                // Thực hiện chèn dữ liệu
                stmt.executeUpdate();

                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while inserting product: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }
    }

    public void deleteProduct(int productId) {
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "DELETE FROM products WHERE productID = ?";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, productId);

                // Thực hiện xóa dữ liệu
                stmt.executeUpdate();
                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while deleting product: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }
    }




}