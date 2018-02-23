package com.yilei.ownerdraw.view.Handtouch;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/23.
 * 绘制椭圆
 */

public class OvalDrawer extends RectDrawer{

    public OvalDrawer(View view) {
        super(view);
    }

    @Override
    protected void drawShape(Canvas canvas, int firstX, int firstY, int currentX, int currentY) {
        super.drawShape(canvas, firstX, firstY, currentX, currentY);
        Paint paint = AttributesTool.getInstance().getPaint();

        if(firstX < currentX && firstY < currentY){
            //↘右下
            canvas.drawOval(new RectF(firstX, firstY, currentX, currentY), paint);
        }else if(firstX < currentX && firstY > currentY){
            //↖右上
            canvas.drawOval(new RectF(firstX, currentY, currentX, firstY), paint);
        }else if(firstX > currentX && firstY < currentY){
            //↙左下
            canvas.drawOval(new RectF(currentX, firstY, firstX, currentY), paint);
        }else if(firstX > currentX && firstY > currentY){
            //↖左上
            canvas.drawOval(new RectF(currentX, currentY, firstX, firstY), paint);
        }
    }
}
