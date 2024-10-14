package com.example.prm392.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.model.Cart;

import java.util.List;

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
}
