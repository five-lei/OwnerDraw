package com.yilei.ownerdraw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yilei.ownerdraw.R;
import com.yilei.ownerdraw.view.MultiLauncher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MultiLaucherActivity extends AppCompatActivity {
    @BindView(R.id.ml)
    MultiLauncher ml;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_laucher);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.pre)
    public void tapPre(){
        ml.moveToPrevious();
    }

    @OnClick(R.id.next)
    public void tapNext(){
        ml.moveToNext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
    }
}
