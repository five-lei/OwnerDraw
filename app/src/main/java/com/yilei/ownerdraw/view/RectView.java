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

public class RectView extends View{

    private int firstX;//起始坐标X
    private int firstY;//起始坐标Y
    private Paint mPaint;
    private Path mPath;

    private Bitmap bitmapBuffer;
    private Canvas bitmapCanvas;
    public RectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(bitmapBuffer == null){
            int width = this.getMeasuredWidth();
            int height = this.getMeasuredHeight();
            bitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmapBuffer);
        }
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
                firstX = x;
                firstY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                mPath.reset();
                if(firstX < x && firstY < y){
                    mPath.addRect(firstX, firstY, x, y, Path.Direction.CCW);
                }else if(firstX > x && firstY < y){
                    mPath.addRect(x, firstY, firstX, y, Path.Direction.CCW);
                }else if(firstX < x && firstY > y){
                    mPath.addRect(firstX, y , x, firstY, Path.Direction.CCW);
                }else if(firstX > x && firstY > y){
                    mPath.addRect(x, y, firstX, firstY, Path.Direction.CCW);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                bitmapCanvas.drawPath(mPath, mPaint);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }
}
