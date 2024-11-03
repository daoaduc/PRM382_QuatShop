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

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    List<Chat> chatList = new ArrayList<>();

    public ChatAdapter(List<Chat> chatList){
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.tvUserName.setText(chat.getUserName());
        holder.tvLastMessage.setText(chat.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        ImageView imgUserAvatar;
        TextView tvUserName;
        TextView tvLastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }

}