package com.yilei.ownerdraw.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.yilei.ownerdraw.R;

public class CornerActivity extends AppCompatActivity {
    private ImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corner);
        iv_img = (ImageView) findViewById(R.id.iv_img);

        Bitmap bmpBuffer = Bitmap.createBitmap(500, 800, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpBuffer);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //原大小绘制
        canvas.drawBitmap(bmp, 0, 0, null);

        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        Rect src = new Rect(0, 0, bmpWidth, bmpHeight);
        Rect dst = new Rect(0, bmpHeight, bmpWidth*3, bmpHeight*3+bmpHeight);
        //从bitmap中扣出一块大小区域为src的图片并绘制到canvas的dst处
        canvas.drawBitmap(bmp, src, dst, null);
        iv_img.setImageBitmap(bmpBuffer);


    }
}
