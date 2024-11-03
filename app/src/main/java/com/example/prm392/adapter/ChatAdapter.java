package com.example.prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.DAO.AccountDAO;
import com.example.prm392.R;
import com.example.prm392.model.Chat;
import com.example.prm392.model.ChatRoom;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    List<ChatRoom> chatRoomList;

    public ChatAdapter(List<ChatRoom> chatRoomList){
        this.chatRoomList = chatRoomList;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatRoom chatRoom = chatRoomList.get(position);
        holder.chatRoomId.setText(String.valueOf(chatRoom.getRoomID()));
        holder.tvChatName.setText(chatRoom.getRoomName());
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView chatRoomId;
        TextView tvChatName;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatRoomId = itemView.findViewById(R.id.chatRoomId);
            tvChatName = itemView.findViewById(R.id.tvChatName);
        }
    }

}