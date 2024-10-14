package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392.R;
import com.example.prm392.model.MainCategoryItemModel;
import com.example.prm392.common.OnItemClickListener;
import java.util.ArrayList;

public class MainCategoryItemAdapter extends RecyclerView.Adapter{
    private ArrayList<MainCategoryItemModel> mList;
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<MainCategoryItemModel> mOnItemClickListener;

    public MainCategoryItemAdapter(Context context, ArrayList<MainCategoryItemModel> list){
        mContext = context;
        mList = list;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    MainCategoryItemModel item = mList.get(position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(item, position);
                    }
                }
            }
        };
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(LayoutInflater.from(mContext).inflate(R.layout.item_main_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // bind name, product ....
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<MainCategoryItemModel> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class Vh extends RecyclerView.ViewHolder{
        public Vh(View itemView){
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
        }
    }
}
