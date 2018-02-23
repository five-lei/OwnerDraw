package com.yilei.ownerdraw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.yilei.ownerdraw.R;
import com.yilei.ownerdraw.view.BaseScrollerViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BaseScrollerActivity extends AppCompatActivity {
    @BindView(R.id.scroller)
    BaseScrollerViewGroup scroller;
    @BindView(R.id.btn_scroll_start)
    Button btn_scroll_start;
    @BindView(R.id.btn_scroll_end)
    Button btn_scroll_end;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_scroller);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_scroll_start)
    public void start(){
        scroller.start();
    }

    @OnClick(R.id.btn_scroll_end)
    public void end(){
        scroller.abort();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
    }
}
