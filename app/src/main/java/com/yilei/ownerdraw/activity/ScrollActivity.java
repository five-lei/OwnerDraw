package com.yilei.ownerdraw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.yilei.ownerdraw.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScrollActivity extends AppCompatActivity {
    @BindView(R.id.activity_scroll)
    LinearLayout activity_scroll;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn_scroll_to)
    Button btn_scroll_to;
    @BindView(R.id.btn_scroll_by)
    Button btn_scroll_by;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        unbinder = ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder == null){
            unbinder.unbind();
        }
    }

    @OnClick(R.id.btn_scroll_to)
    public void btnScrollTo(){
        int x = activity_scroll.getScrollX();
        int y = activity_scroll.getScrollY();
//        tv.scrollTo(x-5, y-5);
        activity_scroll.scrollTo(x-5, y);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_animal);

        btn_scroll_to.startAnimation(animation);
    }

    @OnClick(R.id.btn_scroll_by)
    public void btnScrollBy(){

//        tv.scrollBy(5, 5);
        activity_scroll.scrollBy(5, 0);
    }
}
