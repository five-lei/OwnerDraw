package com.yilei.ownerdraw.view.KLineChart;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilei.ownerdraw.R;
import com.yilei.ownerdraw.view.KLineChart.util.ViewUtil;

/**
 * Created by 易磊 on 2018/2/6.
 * k线中的Tab选项
 */

public class KChartTabView extends RelativeLayout implements View.OnClickListener{
    LinearLayout mLlContainer;//tab
    TextView mTvFullScreen;//切换全屏按钮
    private int mSelectedIndex = 0;//当前选中的下标，默认为0
    private ColorStateList mColorStateList;
    private int mIndicatorColor;//下划线颜色

    private TabSelectListener mTabSelectListener = null;//


    public KChartTabView(Context context) {
        super(context);
        init();
    }

    public KChartTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KChartTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化tab布局
     */
    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab, null, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.Dp2Px(getContext(), 30));
        view.setLayoutParams(layoutParams);
        addView(view);
        mLlContainer = (LinearLayout) findViewById(R.id.ll_container);
        mTvFullScreen = (TextView) findViewById(R.id.tv_fullScreen);
        mTvFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                boolean isVertical = (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
                if(isVertical){
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else{
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
            }
        });
        mTvFullScreen.setSelected(true);
        if(mColorStateList != null){
            mTvFullScreen.setTextColor(mIndicatorColor);
        }

    }

    /**
     * tab item点击事件回调
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(mSelectedIndex >= 0 && mSelectedIndex < mLlContainer.getChildCount()){
            mLlContainer.getChildAt(mSelectedIndex).setSelected(false);
        }
        mSelectedIndex = mLlContainer.indexOfChild(v);//返回选中的tab的下表
        v.setSelected(true);
        mTabSelectListener.onTabSelected(mSelectedIndex);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //requestDisallowInterceptTouchEvent(true)，
        // 致使所有父ViewGroup跳过onInterceptTouchEvent回调，直接dispatchTransformedTouchEvent到当前View
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    public void addTab(String text){
        TabView tabView = new TabView(getContext());
        tabView.setOnClickListener(this);
        tabView.setText(text);
        tabView.setTextColor(mColorStateList);
        tabView.setIndicatorColor(mIndicatorColor);
        mLlContainer.addView(tabView);
        //第一个tab默认选中
        if(mLlContainer.getChildCount() == 1){
            tabView.setSelected(true);
            mSelectedIndex = 0;
            onTabSelected(mSelectedIndex);
        }
    }

    private void onTabSelected(int position){
        if(mTabSelectListener != null){
            mTabSelectListener.onTabSelected(position);
        }
    }

    public void setTextColor(ColorStateList color){
        mColorStateList = color;
        for (int i = 0; i< mLlContainer.getChildCount(); i++){
            TabView tabView = (TabView) mLlContainer.getChildAt(i);
            tabView.setTextColor(mColorStateList);
        }
        if(mColorStateList != null){
            mTvFullScreen.setTextColor(mColorStateList);
        }
    }

    public void setIndicatorColor(int color){
        mIndicatorColor = color;
        for(int i = 0; i < mLlContainer.getChildCount(); i++){
            TabView tabView = (TabView) mLlContainer.getChildAt(i);
            tabView.setIndicatorColor(mIndicatorColor);
        }
    }

    public void setOnTabSelectListener(TabSelectListener listener){
        this.mTabSelectListener = listener;
        //默认选中上一个下标
        if(mLlContainer.getChildCount() > 0 && mTabSelectListener != null){
            mTabSelectListener.onTabSelected(mSelectedIndex);
        }
    }

    interface TabSelectListener{
        /**
         * 选择tab的下标
         * @param position
         */
        void onTabSelected(int position);
    }
}
