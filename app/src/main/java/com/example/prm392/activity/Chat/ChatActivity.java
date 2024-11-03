package com.example.prm392.activity.Chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.adapter.ChatAdapter;
import com.example.prm392.model.Chat;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList = new ArrayList<>();

    private ExecutorService executorService;
    private Socket socket;
    private String SERVER_IP;
    private int SERVER_PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(4);

    }
    // Connect to socket server
    private void connectToSocketServer(){
        executorService.execute(() -> {
            try {
                // Connect to server
                socket = new Socket(SERVER_IP, SERVER_PORT);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    // Load chat to screen
    private void loadChat(){
        chatAdapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(chatAdapter);
    }

    // Load new chat
    private void loadNewChat(){
        chatAdapter.notifyItemInserted(chatList.size() - 1);
    }


}