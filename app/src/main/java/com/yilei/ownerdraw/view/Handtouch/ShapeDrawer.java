package com.yilei.ownerdraw.view.Handtouch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/23.
 */

public abstract class ShapeDrawer {
    private View view;

    public ShapeDrawer(View view){
        super();
        this.view = view;
    }

    public View getView(){
        return view;
    }

    /**
     * 用于绘图
     * @param viewCanvas
     */
    public void draw(Canvas viewCanvas){
        Bitmap bitmap = BitmapBuffer.getInstance().getBitmap();
        viewCanvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * 用于响应触摸事件
     * @param event
     * @return
     */
    public abstract boolean onTouchEvent(MotionEvent event);

    /**
     * 用于绘图的逻辑
     */
    public abstract void logic();


}
