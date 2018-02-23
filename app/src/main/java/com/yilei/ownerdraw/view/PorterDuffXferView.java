package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.yilei.ownerdraw.R;

/**
 * Created by 易磊 on 2018/1/24.
 * 位图运算
 */

public class PorterDuffXferView extends View{

    public PorterDuffXferView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap dst = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Bitmap src = dst.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap b3 = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);

        Canvas c1 = new Canvas(dst);
        Canvas c2 = new Canvas(src);
        Canvas c3 = new Canvas(b3);

        Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.GRAY);
        c1.drawCircle(150, 150, 150, p1);

        Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setColor(Color.GREEN);
        c2.drawRect(0, 0, 300, 300, p2);

        //定义画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //创建图层
        int layer = c3.saveLayer(150, 150, 450, 450, null, Canvas.ALL_SAVE_FLAG);

        //画圆
        c3.drawBitmap(dst, 0, 0, null);
        //定义位图的运算模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //画正方形
        c3.drawBitmap(src, 150, 150, paint);
        //清除位图运算结果
        paint.setXfermode(null);
        //恢复
        c3.restoreToCount(layer);
        //绘制到canvas上
        canvas.drawBitmap(b3, 0, 0, null);

        canvas.translate(0, 500);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo);
        int width = Math.min(image.getWidth(), image.getHeight());//取图片宽高的最小值最为直径
        int r = width / 2;
        Bitmap circle = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);

        Canvas circleCanvas = new Canvas(circle);

        circleCanvas.drawCircle(r, r, r, paint);

        int layers = canvas.saveLayer(0, 0, width, width, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(image, 0, 0, null);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(circle, 0, 0, paint);
        canvas.restoreToCount(layers);


    }
}
