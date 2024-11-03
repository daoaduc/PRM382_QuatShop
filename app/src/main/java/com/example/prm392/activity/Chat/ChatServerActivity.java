package com.example.prm392.activity.Chat;
import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.R;
import com.example.prm392.model.Account;
import com.example.prm392.model.ChatRoom;
import com.example.prm392.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerActivity extends AppCompatActivity {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private HashMap<Integer, Socket> clientSockets = new HashMap<>();
    List<ChatRoom> chatRooms = new ArrayList<>();

    private TextView tvIP, tvPort, tvMessages, tvServerStatus, tvOnlineUsers;
    private EditText etMessage;
    private Button btnSend;
    private String serverIP;
    private static final int SERVER_PORT = 8080;
    private ExecutorService executorService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_server);

        // Assign UI elements
        tvIP = findViewById(R.id.tvIP);
        tvPort = findViewById(R.id.tvPort);
        tvMessages = findViewById(R.id.tvMessages);
        tvServerStatus = findViewById(R.id.tvConnectionStatus);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        // Get server IP address
        try {
            serverIP = getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Initialize ExecutorService
        executorService = Executors.newFixedThreadPool(4);

        // Start server thread
        startServer();

        // Send messages to clients
//        btnSend.setOnClickListener(v -> {
//            if(!etMessage.getText().toString().isEmpty()){
//                sendMessageToClients(etMessage.getText().toString());
//                etMessage.setText("");
//            }else{
//                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void startServer() {
        executorService.execute(() -> {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                runOnUiThread(() -> {
                    tvServerStatus.setText("Server started" +"\n");
                    tvIP.append(serverIP);
                    tvPort.append(String.valueOf(SERVER_PORT));
                });

                while ((clientSocket = serverSocket.accept()) != null) {
                    //get client ip address(optional)
                    String clientIP = clientSocket.getInetAddress().getHostAddress();
                    runOnUiThread(() -> {
                        tvMessages.append(clientIP + " has connected\n");
                    });
                    getRequestFromClient();
                }
            } catch (IOException e) {
                runOnUiThread(() -> tvMessages.setText("Server failed to start"));
                e.printStackTrace();
            }
        });
    }

    private void getRequestFromClient() {
        executorService.execute(() -> {
            try{
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                while ((message = input.readLine()) != null) {
                    String finalMessage = message;
                    runOnUiThread(() -> Log.d("ChatServer", "Received message: " + finalMessage.trim()));
                    // Handle request message
                    if(message.startsWith("ID")){
                        String[] parts = message.split(":");
                        int userId = Integer.parseInt(parts[1]);
                        clientSockets.put(userId, clientSocket);
                        runOnUiThread(() -> tvMessages.append("Client " + clientSocket.getInetAddress().getHostAddress() + " has connected.\nUser ID: " + userId + "\n"));
                    }else if(message.startsWith("CREATE_CHAT")){
                        // Create chat room
                        List<Socket> chatMembers = new ArrayList<>();
                        chatMembers.add(clientSocket);
                        ChatRoom chatRoom = new ChatRoom(chatMembers);
                        chatRooms.add(chatRoom);
                        // Response to client
                        sendToClient("CHAT_ROOM_CREATED : " + chatRooms.size(), clientSocket);
                        runOnUiThread(() -> {
                            tvMessages.append("Chat room created\nRoom id: " + chatRooms.size() + "\n");
                        });
                    }else if(message.startsWith("JOIN_CHAT_ROOM")){
                        // Join chat room
                    }else{
                        // Read as a message
                        String[] parts = message.split("//@//");
                        int roomId = Integer.parseInt(parts[0].trim());
                        String msg = parts[1].trim();
                        // Send msg to chat room members
                        if (roomId >= 0 && roomId < chatRooms.size()) {
                            List<Socket> members = chatRooms.get(roomId).getMembers();
                            for (Socket member : members) {
                                if(member != clientSocket){
                                    sendToClient(message, member);
                                }
                            }
                        } else {
                            Log.e("ChatServer", "Invalid room ID: " + roomId);
                        }

                        runOnUiThread(() -> {
                            tvMessages.append("\nClient " + clientSocket.getInetAddress().getHostAddress() + " has sent: " + msg + "to room " + roomId + "\n");
                            Log.d("ChatServer", "Received message: " + msg.trim() + " to room " + roomId);
                        });
                    }
                }
            } catch (IOException e) {
                runOnUiThread(() -> tvMessages.append(clientSocket.getInetAddress().getHostAddress() + "Client disconnected"));
                e.printStackTrace();
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);

            }
        });
    }

    // Send message to client
    private void sendToClient(String msg, Socket clientSocket){
        executorService.execute(() -> {
            try {
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                output.println(msg);
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            executorService.shutdown(); // Properly shutdown the executor
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }
}