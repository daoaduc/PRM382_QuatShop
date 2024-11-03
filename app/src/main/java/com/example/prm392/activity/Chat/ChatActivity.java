package com.example.prm392.activity.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.adapter.ChatAdapter;
import com.example.prm392.model.Account;
import com.example.prm392.model.Chat;
import com.example.prm392.model.ChatRoom;
import com.example.prm392.model.SocketManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatRoom> chatList = new ArrayList<>();

    int userId;
    int roleId;

    private ExecutorService executorService;
    private Socket socket;
    private final String SERVER_IP = "192.168.36.100";
    private final int SERVER_PORT = 8080;
    FloatingActionButton btnCreateChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btnCreateChat = findViewById(R.id.btn_create_chat);

        // Initialize recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(chatAdapter);

        // Create thread pool
        executorService = Executors.newFixedThreadPool(4);

        // Connect to socket server
        connectToSocketServer();

        // Get userid
        SharedPreferences sharedPreferences = getSharedPreferences("UserIDPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userID", -1);
        new Thread(() -> {
            AccountDAO accountDAO = new AccountDAO();
            Account user = accountDAO.getAccountById(userId);
            roleId = user.getRoleID().getRoleID();
            // Display button for creating chat
            if(roleId == 1){
                runOnUiThread(() -> btnCreateChat.setVisibility(View.VISIBLE));
            }else{
                runOnUiThread(() -> btnCreateChat.setVisibility(View.INVISIBLE));
            }
        }).start();

        if(roleId == 2){

        }

        // Create chat
        btnCreateChat.setOnClickListener(v -> {
            Log.d("ChatActivity", "Create chat button clicked");
            sendRequest("CREATE_CHAT");
        });
    }

    private void sendRequest(String requestMessage) {
        executorService.execute(() -> {
            try {
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                output.println(requestMessage);
                output.flush();
                runOnUiThread(() -> {Log.d("ChatActivity", "Message sent: " + requestMessage);});
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void receiveResponse() {
        executorService.execute(() -> {
            try {
                Scanner input = new Scanner(socket.getInputStream());
                while (input.hasNextLine()) {
                    String responseMessage = input.nextLine();
                    runOnUiThread(() -> {Log.d("ChatActivity", "Message received: " + responseMessage);});
                    if(responseMessage.contains("CHAT_ROOM_CREATED")){
                        String[] chatRoomInfo = responseMessage.split(":");
                        int chatRoomId = Integer.parseInt(chatRoomInfo[1].trim());
                        Log.d("ChatActivity", "Chat room created: " + chatRoomId);
                        chatList.add(new ChatRoom(chatRoomId, "Conversation " + chatRoomId));
                        runOnUiThread(this::loadNewChat);

                    }
                }
            } catch (IOException e) {
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
                SocketManager.setSocket(socket);
                sendRequest("ID:"+userId);
                receiveResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    // Load new chat
    private void loadNewChat(){
        chatAdapter.notifyItemInserted(chatList.size() - 1);
    }


    public void goToChat(View view) {
        String chatRoomId = ((TextView) view.findViewById(R.id.chatRoomId)).getText().toString();
        Intent intent = new Intent(this, MessageActivity.class);
        List<Socket> members = new ArrayList<>();
        intent.putExtra("chatRoom", new ChatRoom(Integer.parseInt(chatRoomId), "Conversation " + chatRoomId));
        startActivity(intent);
    }

    public void changeChatRoomName(String newName){
        //todo: change chat room name
    }
}