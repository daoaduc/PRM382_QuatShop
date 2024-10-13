package com.example.prm392.activity.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public abstract class AbsMainViewHolder {
    protected ViewGroup mParentView;
    protected Context mContext;
    protected View mContentView;
    public AbsMainViewHolder(Context context, ViewGroup parent){
        mContext = context;
        mParentView = parent;
        mContentView = LayoutInflater.from(context).inflate(getLayoutId(), mParentView, false);
        init();
    }
    public void loadData(){}

    protected abstract int getLayoutId();

    public abstract void init();

    protected <T extends View> T findViewById(int res) {
        return mContentView.findViewById(res);
    }

    public void addToParent(){
        if(mParentView != null && mContentView != null){
            ViewParent parent = mContentView.getParent();
            if(parent != null){
                if( parent != mParentView){
                    ((ViewGroup) parent ).removeView(mContentView);
                    mParentView.addView(mContentView);
                }
            } else {
                mParentView.addView(mContentView);
            }
        }
    }

    public void removeFromParent(){
        if (mContentView != null) {
            ViewParent parent = mContentView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mContentView);
            }
        }
    }
}
