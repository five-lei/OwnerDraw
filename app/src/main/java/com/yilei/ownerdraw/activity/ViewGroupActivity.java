package com.yilei.ownerdraw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.yilei.ownerdraw.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewGroupActivity extends AppCompatActivity {
    @BindView(R.id.btn_CornerLayout)
    Button btn_CornerLayout;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_CornerLayout)
    public void btnCornerLayout(){
        Intent intent = new Intent(this, CornerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_FlowLayout)
    public void btnFlowLayout(){
        Intent intent = new Intent(this, FlowActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_ScrollLayout)
    public void btnScrollLayout(){
        Intent intent = new Intent(this, ScrollActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_BaseScroller)
    public void btnBaseScroller(){
        Intent intent = new Intent(this, BaseScrollerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_MultiLauncher)
    public void btnMultiLauncher(){
        Intent intent = new Intent(this, MultiLaucherActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
    }
}
