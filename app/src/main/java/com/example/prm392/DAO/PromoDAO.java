package com.example.prm392.DAO;

import android.util.Log;

import com.example.prm392.ConnectionClass;
import com.example.prm392.model.Promo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromoDAO {
    ConnectionClass connectionClass;

    public PromoDAO() {
        connectionClass = new ConnectionClass();
    }

    // Phương thức để lấy thông tin khuyến mãi dựa trên mã khuyến mãi
    public Promo getPromoByPromoCode(String promoCode) {
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "SELECT promoID, promoCode, promoType, promoValue, startDate, endDate, maxUsage, usedCount, status " +
                    "FROM promo WHERE promoCode = ? AND status = 1 AND CURRENT_DATE() < endDate  AND (maxUsage - usedCount) > 0;";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, promoCode);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Promo promo = new Promo();
                    promo.setPromoID(rs.getInt("promoID"));
                    promo.setPromoCode(rs.getString("promoCode"));
                    promo.setPromoType(rs.getBoolean("promoType"));
                    promo.setPromoValue(rs.getLong("promoValue"));
                    promo.setStartDate(rs.getDate("startDate"));
                    promo.setEndDate(rs.getDate("endDate"));
                    promo.setMaxUsage(rs.getInt("maxUsage"));
                    promo.setUsedCount(rs.getInt("usedCount"));
                    promo.setStatus(rs.getBoolean("status"));
                    return promo;
                }

                rs.close();
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                Log.e("ERROR", "Error while fetching promo by promoCode: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }

        return null;
    }

    public boolean incrementUsageCount(int promoID) {
        Connection connection = connectionClass.CONN();

        if (connection != null) {
            String query = "UPDATE promo SET usedCount = usedCount + 1 WHERE promoID = ? AND status = 1 AND CURRENT_DATE() < endDate  AND (maxUsage - usedCount) > 0;";
            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, promoID);
                int rowsAffected = stmt.executeUpdate();

                stmt.close();
                connection.close();

                return rowsAffected > 0;
            } catch (SQLException e) {
                Log.e("ERROR", "Error while updating used count for promoCode: " + e.getMessage());
            }
        } else {
            Log.e("ERROR", "Connection to database failed");
        }

        return false;
    }

}
