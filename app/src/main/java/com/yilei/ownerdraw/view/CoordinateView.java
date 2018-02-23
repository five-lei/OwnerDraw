package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yilei on 2018/1/19.
 * 画布canvas坐标转换，转换的方式有4中，平移，旋转，缩放，拉斜
 * 1.坐标平移：public void translate(float dx, float dy) 在当前原点的基础上水平移动dx距离，垂直移动dy距离，正负决定方向。
 * 坐标原点改变后，所有的坐标都是以新的原点为参照进行定位。
 * 2.坐标缩放：public void scale(float sx; float sy) sx和sy分别是x方向和y方向的缩放比例，
 * 小于1表示缩放，等于1表示保持原样，大于1表示放大，画布缩放后，画布上的图形也会等比例缩放
 *            public void scale(float sx, float sy, float px, float py) 以(px, py)为中心对画布进行缩放
 * 3.坐标旋转 public void rotate(float degrees, float px, float py) 以(px ,py)为中心对画布坐标旋转degrees度，为正表示顺时针，为负表示逆时针
 */

public class CoordinateView extends View{


    public CoordinateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置画笔抗锯齿
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.save();//保存现场
        for (int i = 0; i < 10; i++){
            canvas.drawRect(0, 0, 100, 100, paint);
            canvas.translate(20, 20);// 向x移动50，向y移动50，等同canvas.drawRect(50, 50, 200, 200, paint);
        }
        canvas.restore();//恢复现场

        //平移坐标，只移动y，距离上面图形350-(10+20*10)
        canvas.translate(0, 300);
        canvas.save();

        for(int i = 0; i < 10; i++){
            canvas.drawRect(0, 0, 400, 400, paint);
            canvas.scale(0.9f, 0.9f, 200, 200);//以(200, 200)为中心进行缩放，即以矩形的中心每次缩放0.9
        }

        canvas.restore();

        canvas.translate(0, 420);
        canvas.save();

        canvas.drawCircle(200, 200, 200, paint);

        for (int i = 0; i < 12; i++){
            canvas.drawLine(350, 200, 400, 200, paint);
            canvas.rotate(30, 200, 200);
        }

        canvas.restore();








    }
}
