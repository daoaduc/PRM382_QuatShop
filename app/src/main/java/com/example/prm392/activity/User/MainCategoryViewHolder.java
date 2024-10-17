package com.example.prm392.activity.User;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.example.prm392.R;

public class MainCategoryViewHolder extends AbsMainViewHolder{

    public MainCategoryViewHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

// Hàm này để set layout cho view này
    @Override
    protected int getLayoutId() {
        return R.layout.view_main_category;
    }

// Hàm này xử lý setting cho view (activity) này sẽ bắt đầu viết ở đây ( tương tự như hàm onCreate thường dùng )
// ví dụ:
//    connectionClass = new ConnectionClass();
//    edtFullname = findViewById(R.id.edtFullname)
    @Override
    public void init() {

    }

// Hàm này sẽ tự động được gọi khi chuyển sang tab này
// Đây là nơi xử lý data cho view này nhé
    @Override
    public void loadData(){

    }
}
