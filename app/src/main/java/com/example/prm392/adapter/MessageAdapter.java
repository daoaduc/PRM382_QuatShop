//package com.example.prm392.adapter;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.prm392.R;
//
//import java.util.List;
//
//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
//    private List<Message> messageList;
//    private SharedPreferences sharedPreferences;
//
//    // constructor
//    public MessageAdapter(List<Message> messageList, Context context) {
//        this.messageList = messageList;
//        this.sharedPreferences = context.getSharedPreferences("UserIDPrefs", Context.MODE_PRIVATE);
//    }
//
//    @NonNull
//    @Override
//    public  MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_message, parent, false);
//        return new MessageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        Message message = messageList.get(position);
//
//        holder.tvUserChat.setText(message.getContent());
//
//        LinearLayout.LayoutParams paramUserName = (LinearLayout.LayoutParams) holder.tvUserName.getLayoutParams();
//        LinearLayout.LayoutParams paramsUserChat = (LinearLayout.LayoutParams) holder.tvUserChat.getLayoutParams();
//
//        // Set message style based on sender
//        if(message.getSender().getAccID() == sharedPreferences.getInt("userID", -1)){
//            holder.tvUserName.setText("You");
//            paramUserName.gravity = Gravity.END;
//            paramsUserChat.gravity = Gravity.END;
//            holder.tvUserChat.setBackgroundResource(R.drawable.bg_my_chat);
//        }else{
//            holder.tvUserName.setText(message.getSender().getFullname());
//            paramUserName.gravity = Gravity.START;
//            paramsUserChat.gravity = Gravity.START;
//            holder.tvUserChat.setBackgroundResource(R.drawable.bg_other_user_chat);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return messageList.size();
//    }
//
//    public static class MessageViewHolder extends RecyclerView.ViewHolder {
//        TextView tvUserName;
//        TextView tvUserChat;
//
//        public MessageViewHolder(@NonNull View v) {
//            super(v);
//            tvUserName = v.findViewById(R.id.tvUserName);
//            tvUserChat = v.findViewById(R.id.tvUserChat);
//        }
//    }
//
//}
