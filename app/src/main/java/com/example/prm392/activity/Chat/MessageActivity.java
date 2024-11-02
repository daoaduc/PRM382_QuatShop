package com.example.prm392.activity.Chat;

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
import com.example.prm392.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
    Account receiver;
    EditText etMessage;
    Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Get user id
        SharedPreferences sharedPreferences = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userID", -1);

        // Initialize recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Load all user messages
        loadMessage();
        messageAdapter = new MessageAdapter(messageList, this);
        recyclerView.setAdapter(messageAdapter);

        // Create thread pool for socket activities
        executorService = Executors.newFixedThreadPool(4);

        // Connect to server
        connectToSocketServer();

        new Thread(() -> {
            AccountDAO accountDAO = new AccountDAO();
            user = accountDAO.getAccountById(userId);
            receiver = accountDAO.getAccountById(1);
        }).start();

        // Send message
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString();
            if (!message.isEmpty()) {
                sendMessage(new Message(user, receiver, message, new Date(), false));
                etMessage.setText("");
            }
        });
    }

    // Receive message
    private void receiveMessage() {
        executorService.execute(() -> {
            try {
                // Receive message
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message;
                while ((message = (Message) ois.readObject()) != null) {
                    Message finalMessage = message;
                    runOnUiThread(() -> {
                        messageList.add(finalMessage);
                        loadNewMessage();
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    // Send message
    private void sendMessage(Message message) {
        executorService.execute(() -> {
            try {
                //Send message
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(message);
                oos.flush();
                runOnUiThread(() -> {
                    messageList.add(message);
                    loadNewMessage();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Connect to socket server
    private void connectToSocketServer(){
        executorService.execute(() -> {
            try {
                // Connect to server
                socket = new Socket(SERVER_IP, SERVER_PORT);
                receiveMessage();
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(MessageActivity.this, "Server not found", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        });
    }
    // Load new message
    private void loadNewMessage(){
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    // Load all user messages
    private void loadMessage(){
        // to do
    }
}