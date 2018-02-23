package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yilei on 2018/1/19.
 *
 */

public class WatchView extends View{

    private Paint mPaint;
    private Calendar calendar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public WatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);

        calendar = Calendar.getInstance();
    }

    public void run(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
            }
        }, 0, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取组件宽度
        int width = this.getMeasuredWidth();
        //获取组件高度
        int height = this.getMeasuredHeight();
        //直径取小值
        int len = Math.min(width, height);


        //绘制表盘
        drawPlate(canvas, len);
        //绘制指针
        drawPoints(canvas, len);
    }

    public void drawPlate(Canvas canvas, int len){
        canvas.save();
        //画园
        int r = len / 2;
        canvas.drawCircle(r, r, r, mPaint);

        //画刻度，一共有60根
        for(int i = 0; i < 60; i++){
            if(i % 5 == 0){
                //画长刻度，长刻度为半径的1/10,
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(4);
                canvas.drawLine(r + 9 * r / 10, r, 2*r, r, mPaint);
            }else{
                //画短刻度， 短刻度为半径的1/15
                mPaint.setColor(Color.GREEN);
                mPaint.setStrokeWidth(2);
                canvas.drawLine(r + 14 * r / 15, r, 2*r, r, mPaint);
            }

            //以(r, r)为中心，将画布旋转6度，画出60根刻度
            canvas.rotate(6, r, r);

        }
        canvas.restore();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void drawPoints(Canvas canvas, int len){
        //先获取系统时间 将一个日历实例的时间设置为当前系统时间
        calendar.setTimeInMillis(System.currentTimeMillis());
        //获取时分秒
        int hours = calendar.get(Calendar.HOUR) % 12;//转换为12小时制
        Log.v("WatchView", "hours = "+ hours);
        int minutes = calendar.get(Calendar.MINUTE);
        Log.v("WatchView", "minutes = "+ minutes);
        int seconds = calendar.get(Calendar.SECOND);
        Log.v("WatchView", "seconds = "+ seconds);

        //1. 画时针
        //角度，顺时针
        int degree = 360 / 12 * hours;
        //转换为弧度
        double radians = Math.toRadians(degree);
        //根据当前时计算两个点的坐标
        int r = len / 2;
        int startX = r;
        int startY = r;
        //时针为半径的1/2; cos(radians)为x轴离圆心的距离，sin(radians)为y轴离圆心的距离
        int stopX = (int) (r + r * 0.5 * Math.cos(radians));
        int stopY = (int) (r + r * 0.5 * Math.sin(radians));

        canvas.save();
        mPaint.setStrokeWidth(3);
        //因为0度从3点开始，时间从12点开始，所以要将画布逆时针旋转90度
        canvas.rotate(-90, r, r);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        canvas.restore();

        //2.画分针
        //计算角度
        degree = 360 / 60 * minutes;
        radians = Math.toRadians(degree);
        //分针为半径的3/5
        stopX = (int) (r + r * 0.6 * Math.cos(radians));
        stopY = (int) (r + r * 0.6 * Math.sin(radians));
        canvas.save();
        mPaint.setStrokeWidth(2);
        canvas.rotate(-90, r, r);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        canvas.restore();

        //3.画秒针
        //计算角度
        degree = 360 / 60 * seconds;
        radians = Math.toRadians(degree);
        //秒针为半径的4/5
        stopX = (int) (r + r * 0.8 * Math.cos(radians));
        stopY = (int) (r + r * 0.8 * Math.sin(radians));
        canvas.save();
        mPaint.setStrokeWidth(1);
        canvas.rotate(-90, r, r);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        //在给秒钟加个尾巴
        radians = Math.toRadians(degree - 180);
        stopX = (int) (startX + r * 0.15 * Math.cos(radians));
        stopY = (int) (startY + r * 0.15 * Math.sin(radians));
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        canvas.restore();

    }
}
