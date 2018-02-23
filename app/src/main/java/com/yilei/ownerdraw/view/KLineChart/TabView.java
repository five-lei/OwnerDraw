package com.yilei.ownerdraw.view.KLineChart;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilei.ownerdraw.R;

/**
 * Created by 易磊 on 2018/2/6.
 * k线tab的选项item
 */

public class TabView extends RelativeLayout{
    private TextView tab_text;
    private View indicator;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(LayoutInflater.from(context).inflate(R.layout.item_tab, null));
        tab_text = (TextView) findViewById(R.id.tab_text);
        indicator = findViewById(R.id.indicator);
    }

    public void setTextColor(ColorStateList color){
        if(color != null){
            tab_text.setTextColor(color);
        }
    }

    public void setText(String text){
        tab_text.setText(text);
    }

    public void setIndicatorColor(int color){
        indicator.setBackgroundColor(color);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        indicator.setVisibility(selected ? VISIBLE : INVISIBLE);
    }
}
