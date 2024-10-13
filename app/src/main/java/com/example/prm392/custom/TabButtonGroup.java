package com.example.prm392.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class TabButtonGroup extends LinearLayout implements View.OnClickListener {
    private Button[] mButtons;
    private ViewPager mViewPager;
    private int mCurPosition;

    public TabButtonGroup(Context context) {
        this(context, null);
    }
    public TabButtonGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButtonGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setCurPosition(int position) {
//        if (position == mCurPosition) {
//            if(mOnBtnClickListener!=null){
//                mOnBtnClickListener.onClick(position);
//            }
//            return;
//        }
//        if (mClickIntercepter != null && mClickIntercepter.needIntercept(position)) {
//            return;
//        }
//        mTabButtons[mCurPosition].setChecked(false);
//        mTabButtons[position].setChecked(true);
//        mCurPosition = position;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount > 0) {
            mButtons = new Button[childCount];
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                v.setTag(i);
                v.setOnClickListener(this);
                mButtons[i] = (Button) v;
            }
        }
    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            setCurPosition((int) tag);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }
}
