package com.yilei.ownerdraw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yilei.ownerdraw.R;
import com.yilei.ownerdraw.view.WatchView;

public class WatchActivity extends AppCompatActivity {
    private WatchView watchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        watchView = (WatchView) findViewById(R.id.watch_view);
        watchView.run();
    }
}
