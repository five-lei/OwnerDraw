package com.yilei.ownerdraw.view.Handtouch;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by yilei on 2018/1/22.
 * 绘图缓冲区
 */

public class BitmapBuffer {
    private Bitmap bitmap;
    private Canvas canvas;
    public static BitmapBuffer self;

    public BitmapBuffer(int width, int height){
        init(width, height);
    }

    private void init(int width, int height){
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
    }

    public static BitmapBuffer getInstance(){
        if(self == null){
            self = new BitmapBuffer(SystemParams.areaWidth, SystemParams.areaHeight);
        }
        return self;
    }

    /**
     * 获取缓冲区的画布
     * @return
     */
    public Canvas getCanvas(){
        return canvas;
    }

    /**
     * 获得绘图结果
     * @return
     */
    public Bitmap getBitmap(){
        return bitmap;
    }

    /**
     * 将当前的绘图结果保存到栈中
     */
    public void pushBitmap(){
        BitmapHistory.getInstance().pushBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, false));
    }

    public void redo(){
        BitmapHistory history = BitmapHistory.getInstance();
        if(history.isRedo()){
            Bitmap bmp = history.reDo();
            if(bmp != null){
                bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                //必须重新关联画布
                canvas.setBitmap(bitmap);
                if(!bmp.isRecycled()){
                    bmp.recycle();
                    System.gc();
                    bmp = null;
                }
            }
        }
    }


}
