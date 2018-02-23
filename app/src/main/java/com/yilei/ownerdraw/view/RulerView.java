package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/22.
 * 直尺
 */

public class RulerView extends View{
    private Paint mPaint;

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(14f);
        mPaint.setStrokeWidth(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取组件宽度
        int width = this.getMeasuredWidth();
        int right = width+getPaddingLeft()+getPaddingRight();
        //画圆角矩形
        canvas.drawRoundRect(0, 0, right, 150, 5, 5, mPaint);

        canvas.save();

        canvas.translate(20, 0);
        int line = width / 131;
        for (int i = 0; i < 131; i++){

            if(i % 10 == 0){
                canvas.drawLine(i*line, 100, i*line, 150, mPaint);
                canvas.drawText(String.valueOf(i/10), i*line, 80, mPaint);
            } else if(i % 5 == 0 && i > 0){
                canvas.drawLine(i*line, 115, i*line, 150, mPaint);
            }else{
                canvas.drawLine(i*line, 120, i*line , 150, mPaint);
            }

        }
        canvas.restore();
    }
}
