package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.model.Cart;

import java.util.List;

@Dao
public interface CartDAO {
    @Insert
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);

    @Query("SELECT * FROM cart")
    List<Cart> getAllCartItems();

    @Query("DELETE FROM cart")
    void deleteAllCartItems();

    @Query("DELETE FROM cart WHERE userId = :userId")
    void deleteAllCartItemsByUserId(int userId);

    @Query("SELECT * FROM cart WHERE productId = :productId AND userId = :userId LIMIT 1")
    Cart getCartItemByProductIdAndUserId(int productId, int userId);

    @Query("SELECT * FROM cart WHERE userId = :userId")
    List<Cart> getAllCartItemsByUserId(int userId);
}
