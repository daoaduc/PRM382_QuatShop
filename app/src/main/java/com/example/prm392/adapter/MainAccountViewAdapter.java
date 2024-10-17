package com.example.prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.model.OptionItem;

import java.util.List;

public class MainAccountViewAdapter extends RecyclerView.Adapter<MainAccountViewAdapter.ViewHolder>{
    private List<OptionItem> optionList;
    private static OnItemClickListener listener;

    public MainAccountViewAdapter(List<OptionItem> optionList){
        this.optionList = optionList;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView optionIcon;
        public TextView optionText;

        public ViewHolder(View itemView) {
            super(itemView);
            optionIcon = itemView.findViewById(R.id.optionIcon);
            optionText = itemView.findViewById(R.id.optionText);
            // Handle click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public MainAccountViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_option_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAccountViewAdapter.ViewHolder holder, int position) {
        // Get the current option item
        OptionItem currentItem = optionList.get(position);

        // Set the icon and name
        holder.optionIcon.setImageResource(currentItem.getOptionIcon());
        holder.optionText.setText(currentItem.getOptionName());
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }
    // Click Listener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
