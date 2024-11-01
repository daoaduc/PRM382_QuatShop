package com.example.prm392.activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.prm392.ConnectionClass;
import com.example.prm392.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ConnectionClass connectionClass;

    Connection con;

    ResultSet rs;

    String name, str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionClass = new ConnectionClass();
        connect();
    }

    public void btnClick(View view){


    }

     public void connect(){
         ExecutorService executorService = Executors.newSingleThreadExecutor();
         executorService.execute(()->{
             try {
                 con = connectionClass.CONN();
                 if(con == null){
                     str = "Error in connection with MySQL";
                 }else{
                     str = "Connected";
                 }
             }catch (Exception e){
                 throw new RuntimeException(e);
             }

             runOnUiThread(()->{
                 try{
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
             });
         });
     }
}