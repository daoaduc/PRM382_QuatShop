package com.example.prm392.activity.User;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.prm392.R;
import com.example.prm392.adapter.ViewPagerAdapter;
import com.example.prm392.custom.TabButtonGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewHolder extends AppCompatActivity {
    private TabButtonGroup mTabButtonGroup;
    private ViewPager mViewPager;
    private AbsMainViewHolder[] mViewHolders;
    private List<FrameLayout> mViewList;
    private MainHomeViewHolder mMainHomeViewHolder;
    private MainCategoryViewHolder mMainCategoryViewHolder;
    private MainCartViewHolder mMainCartViewHolder;
    private MainAccountViewHolder mMainAccountViewHolder;

    private boolean mFirstLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_holder);
        loadSettingControl();
    }

    private void loadSettingControl(){
        mTabButtonGroup = (TabButtonGroup) findViewById(R.id.tab_group);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);
        mViewList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViewList.add(frameLayout);
        }
        mViewPager.setAdapter(new ViewPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadPageData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabButtonGroup.setViewPager(mViewPager);
        mViewHolders = new AbsMainViewHolder[4];
        mFirstLoad = true;
    }

    private void loadPageData(int position){
        if(mViewHolders == null){
            return;
        }
        AbsMainViewHolder vh = mViewHolders[position];
        if(vh == null){
            if(position < 4){
                FrameLayout parent = mViewList.get(position);
                if(position == 0){
                    mMainHomeViewHolder = new MainHomeViewHolder(this, parent);
                    vh = mMainHomeViewHolder;
                } else if(position == 1){
                    mMainCategoryViewHolder = new MainCategoryViewHolder(this, parent);
                    vh = mMainCategoryViewHolder;
                }
                else if(position == 2){
                    mMainCartViewHolder = new MainCartViewHolder(this, parent);
                    vh = mMainCartViewHolder;
                }
                else if(position == 3){
                    mMainAccountViewHolder = new MainAccountViewHolder(this, parent);
                    vh = mMainAccountViewHolder;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
            }
        }
        if(vh != null){
            vh.loadData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mFirstLoad){
            mFirstLoad = false;
            loadPageData(0);
        }
    }
}
