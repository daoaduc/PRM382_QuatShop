package com.example.prm392.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prm392.model.Cart;

@Database(entities = {Cart.class}, version = 2, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();

    private static CartDatabase instance;

    public static synchronized CartDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CartDatabase.class, "cart_database1")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
