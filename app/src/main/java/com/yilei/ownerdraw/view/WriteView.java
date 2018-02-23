package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/22.
 */

public class WriteView extends View{
    private int preX;//上一个点的X坐标
    private int preY;//上一个点的Y坐标;
//    private int currentX;//当前点的X坐标
//    private int currentY;//当前点的Y坐标
    /**
     * bitmap缓存区
     */
    private Bitmap bitmapBuffer;
    private Canvas bitmapCanvas;
    private Paint mPaint;
    private Path mPath;


    public WriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = this.getMeasuredWidth();//获取当前组件的宽度
        int height = this.getMeasuredHeight();//获取当前组件的高度
        //新建bitmap对象
        bitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmapBuffer);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapBuffer, 0, 0, null);
        canvas.drawPath(mPath, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                //手指按下，记录第一个点的坐标
                preX = x;
                preY = y;
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                //手指移动，只显示绘制过程
                //使用贝塞尔曲线经行绘图，需要一个起点(preX, preY)，一个终点(x,y),一个控制点((preX+x)/2, (preY+y)/2)
//                currentX = x;
//                currentY = y;
                int controlX = (preX+x)/2;
                int controlY = (preY+y)/2;
//                mPath.quadTo(preX, preY, x, y);
                mPath.quadTo(controlX, controlY, x, y);
//                bitmapCanvas.drawLine(preX, preY, currentX, currentY, mPaint);
                this.invalidate();
                //当前点的坐标成为下一个点的起始坐标
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_UP:
                //手指松开后将最终的绘图结果绘制在bitmapBuffer中，同时绘制在view上
                bitmapCanvas.drawPath(mPath, mPaint);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }
}
