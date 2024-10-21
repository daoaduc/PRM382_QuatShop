package com.example.prm392.DAO;

import android.util.Log;

import com.example.prm392.ConnectionClass;
import com.example.prm392.common.Constants;
import com.example.prm392.model.Product;
import com.example.prm392.model.ProductStatus;
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

    public void updateProduct(Product product) {
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "UPDATE products SET productName = ?, quantity = ?, description = ?, price = ?, productIMG = ?, status = ? WHERE productID = ?";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, product.getProductName());
                stmt.setInt(2, product.getQuantity());
                stmt.setString(3, product.getDescription());
                stmt.setLong(4, product.getPrice());
                stmt.setString(5, product.getProductIMG());
                stmt.setInt(6, product.getStatus().getStatusID());
                stmt.setInt(7, product.getProductID());

                // Thực hiện cập nhật dữ liệu
                stmt.executeUpdate();
                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while updating product: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }
    }

    public int getLastProductID() {
        Connection connection = connectionClass.CONN();
        int lastProductID = -1;  // Biến để lưu trữ productID cuối cùng, mặc định là -1 nếu không tìm thấy

        if (connection != null) {
            String query = "SELECT MAX(productID) AS lastProductID FROM products";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    lastProductID = rs.getInt("lastProductID");  // Lấy productID cuối cùng
                }

                rs.close();
                stmt.close();
                connection.close();
            } catch (Exception e) {
                Log.e("ERROR", "Error while fetching last productID: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }

        return lastProductID;
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

    public List<Product> getProductsBySearching(String productName, Integer productID) {
        List<Product> productList = new ArrayList<>();
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            StringBuilder queryBuilder = new StringBuilder("SELECT productID, productName, price, productIMG FROM products WHERE 1=1");

            if (productName != null && !productName.isEmpty()) {
                queryBuilder.append(" AND productName LIKE ?");
            }
            if (productID != null && productID != Constants.ALL_BTN_CATEGORY) {
                queryBuilder.append(" AND categoryID = ?");
            }

            try {
                PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString());

                int paramIndex = 1;
                if (productName != null && !productName.isEmpty()) {
                    stmt.setString(paramIndex++, "%" + productName + "%");
                }
                if (productID != null && productID != Constants.ALL_BTN_CATEGORY) {
                    stmt.setInt(paramIndex++, productID);
                }

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