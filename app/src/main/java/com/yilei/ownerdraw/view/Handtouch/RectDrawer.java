package com.yilei.ownerdraw.view.Handtouch;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/23.
 * 矩形的绘制
 */

public class RectDrawer extends ShapeDrawer{
    private int firstX;//起始点的X坐标
    private int firstY;//起始点的Y坐标
    private int currentX;//当前点的X坐标
    private int currentY;//当前点的Y坐标

    public RectDrawer(View view){
        super(view);
    }

    @Override
    public void draw(Canvas viewCanvas) {
        super.draw(viewCanvas);
        drawShape(viewCanvas, firstX, firstY, currentX, currentY);
    }

    /**
     * 画当前的形状
     * @param canvas
     * @param firstX
     * @param firstY
     * @param currentX
     * @param currentY
     */
    protected void drawShape(Canvas canvas, int firstX, int firstY, int currentX, int currentY){
        Paint paint = AttributesTool.getInstance().getPaint();

        //判断手指的方向
        if(firstX < currentX && firstY < currentY){
            //↘右下
            canvas.drawRect(firstX, firstY, currentX, currentY, paint);
        }else if(firstX < currentX && firstY > currentY){
            //↗右上
            canvas.drawRect(firstX, currentY, currentX, firstY, paint);
        }else if(firstX > currentX && firstY < currentY){
            //↙左下
            canvas.drawRect(currentX, firstY, firstX, currentY, paint);
        }else if(firstX > currentX && firstY > currentY){
            //↖左上
            canvas.drawRect(currentX, currentY, firstX, firstY, paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstX = x;
                firstY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = x;
                currentY = y;
                getView().invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //将最终的矩形绘制在缓冲区
                Canvas canvas = BitmapBuffer.getInstance().getCanvas();
                drawShape(canvas, firstX, firstY, currentX, currentY);
                getView().invalidate();
                //保存到撤消栈中
                BitmapBuffer.getInstance().pushBitmap();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void logic() {

    }
}
