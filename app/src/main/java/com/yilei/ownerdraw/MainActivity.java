package com.yilei.ownerdraw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yilei.ownerdraw.activity.FiveChessActivity;
import com.yilei.ownerdraw.activity.GradientActivity;
import com.yilei.ownerdraw.activity.LinghtDiskActivity;
import com.yilei.ownerdraw.activity.MTextViewActivity;
import com.yilei.ownerdraw.activity.PorterDuffXferActivity;
import com.yilei.ownerdraw.activity.RectActivity;
import com.yilei.ownerdraw.activity.RulerActivity;
import com.yilei.ownerdraw.activity.ScratchTicketActivity;
import com.yilei.ownerdraw.activity.ShaderActivity;
import com.yilei.ownerdraw.activity.ViewGroupActivity;
import com.yilei.ownerdraw.activity.WatchActivity;
import com.yilei.ownerdraw.activity.WriteActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.my_boll)
    RotateBoll my_boll;
    @BindView(R.id.clip_view)
    ClipView clipView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                my_boll.postInvalidate();
            }
        }, 200, 50);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                clipView.postInvalidate();
            }
        }, 200, 100);
    }

    @OnClick(R.id.btn_watch)
    public void btnWatch(){
        Intent intent = new Intent(this, WatchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_ruler)
    public void btnRuler(){
        Intent intent = new Intent(this, RulerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_write)
    public void btnWrite(){
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_rect)
    public void btnRecr(){
        Intent intent = new Intent(this, RectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_shader)
    public void btnShader(){
        Intent intent = new Intent(this, ShaderActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_gradient)
    public void btnGradient(){
        Intent intent = new Intent(this, GradientActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_chess)
    public void btnChess(){
        Intent intent = new Intent(this, FiveChessActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_light_disk)
    public void btnLightDisk(){
        Intent intent = new Intent(this, LinghtDiskActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_porter_duff)
    public void btnPorterDuff(){
        Intent intent = new Intent(this, PorterDuffXferActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_scratch_ticket)
    public void btnScratch(){
        Intent intent = new Intent(this, ScratchTicketActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_view_group)
    public void btnViewGroup(){
        Intent intent = new Intent(this, ViewGroupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_text)
    public void btnText(){
        Intent intent = new Intent(this, MTextViewActivity.class);
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
