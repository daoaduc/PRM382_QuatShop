package com.example.prm392.activity.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.MessageAdapter;
import com.example.prm392.model.Account;
import com.example.prm392.model.ChatRoom;
import com.example.prm392.model.Message;
import com.example.prm392.model.SocketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageActivity extends AppCompatActivity {
    ExecutorService executorService;
    Socket socket;
    final String SERVER_IP = "192.168.36.100";
    final int SERVER_PORT = 8080;

    RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();

    int userId;
    Account user;
    Account user2;
    EditText etMessage;
    Button btnSend;

    ChatRoom chatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Get user
        SharedPreferences sharedPreferences = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userID", -1);
        new Thread(() -> {
            AccountDAO accountDAO = new AccountDAO();
            user = accountDAO.getAccountById(userId);
        }).start();

        // Get user2
        //todo: get user2 from intent

        // Initialize recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize message adapter
        messageAdapter = new MessageAdapter(messageList, this);
        recyclerView.setAdapter(messageAdapter);

        // Create thread pool for socket activities
        executorService = Executors.newFixedThreadPool(4);

        //init chat room
        Intent intent = getIntent();
        chatRoom = (ChatRoom) intent.getSerializableExtra("chatRoom");
        socket = SocketManager.getSocket();

        receiveMessage();

        // Send message
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString();
            if (!message.isEmpty()) {
                sendMessage(chatRoom.getRoomID() + "//@//" + message);
                etMessage.setText("");
            }
        });
    }
    // Send message
    private void sendMessage(String message){
        executorService.execute(() -> {
            try {
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                output.println(message);
                output.flush();
                messageList.add(new Message(chatRoom.getRoomID(), user, message.split("//@//")[1]));
                runOnUiThread(this::loadNewMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Receive message
    private void receiveMessage(){
        executorService.execute(() -> {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    String message = input.readLine();
                    String[] messageParts = message.split("//@//");
                    if(messageParts[0].equals(String.valueOf(chatRoom.getRoomID()))){
                        messageList.add(new Message(chatRoom.getRoomID(), null, message.split("//@//")[1]));
                        runOnUiThread(this::loadNewMessage);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Load new message
    private void loadNewMessage(){
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

}