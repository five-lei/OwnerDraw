package com.yilei.ownerdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yilei on 2018/1/19.
 * 1.剪贴区
 * public boolean clipRect(Rect rect)
 * public boolean clipRect(RectF rect)
 * public boolean clipRect(float left, float top, float right, float bottom)
 * public boolean clipRect(int left, int top, int right, int bottom)
 * 以上 4 个方法定义一个矩形的剪切区，参数在前面章节中都有详细介绍，不再赘述。
 * public boolean clipPath(Path path)
 * 以上方法定义一个 Path 剪切区，用于定义更加复杂的区域。
 *
 * 2.与剪切区 Op 运算相关的方法如下：
 *  public boolean clipRect(RectF rect, Op op)
 *  public boolean clipRect(Rect rect, Op op)
 *  public boolean clipRect(float left, float top, float right, float bottom, Op op)
 *  public boolean clipPath(Path path, Op op)
 */

public class ClipView extends View{
    //显示第i涨小图片
    private int i = 0;
    private Bitmap bitmapBoom;

    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapBoom = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_boon);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo);
//        //绘制完整图片
//        canvas.drawBitmap(bitmap, 0, 0, null);
//
//        //平移坐标
//        canvas.translate(0, 500);
//        //定义剪切区
//        canvas.clipRect(100, 100, 400, 400);
//
//        //定义一个新的剪切区,与上一剪切区做Op运算
//        Path path = new Path();
//        path.addCircle(400, 300, 150, Path.Direction.CCW);//CCW标题逆时针的，CW表示顺时针的
//        canvas.clipPath(path, Region.Op.UNION);
//        //再次绘制图片
//        canvas.drawBitmap(bitmap, 0, 0, null);

        //获取位置的宽度和高度
        int width = bitmapBoom.getWidth();
        int height = bitmapBoom.getHeight();

        //剪切区
        int frameWidth = width / 7;
        Rect rect = new Rect(0, 0, frameWidth, height);

        canvas.save();
        canvas.translate(100, 100);//平移坐标
        canvas.clipRect(rect);//设置剪切区
        canvas.drawBitmap(bitmapBoom, -i * frameWidth, 0, null);
        canvas.restore();

        i++;//i加1以播放下一帧
        if(i == 7) i=0;//播放完毕后，重置为0重新播放

    }
}
