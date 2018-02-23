package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yilei.ownerdraw.R;

import java.util.Random;

/**
 * Created by 易磊 on 2018/1/25.
 */

public class ScratchTicketView extends View{
    private Random random;
    private Paint mPaint;
    private Paint clearPaint;
    private static final String[] PRIZE = {
            "恭喜，您中了一等奖，奖金 1 亿元",
            "恭喜，您中了二等奖，奖金 5000 万元",
            "恭喜，您中了三等奖，奖金 100 元",
            "很遗憾，您没有中奖，继续加油哦"
    };

    //缓冲区
    private Bitmap bmpBuffer;
    //缓冲区的画布
    private Canvas cvsBuffer;
    //涂抹的粗细
    private static final int FINGER = 20;
    private int currentX;
    private int currentY;

    public ScratchTicketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        random = new Random();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(20);

        clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置位图运算模式
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //设置线段处的连接样式，有三种1.MITER(锐角连接) 2.ROUND(圆弧连接) 3.BEVEL(斜接，就是把锐角替换成斜边)
        clearPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置线帽, 1.BUTT(无线帽) 2.ROUND(圆形线帽) 3.SQUARE(方形线帽)
        // Paint.Cap.BUTT   Paint.Cap.ROUND  Paint.Cap.SQUARE
        clearPaint.setStrokeCap(Paint.Cap.ROUND);
        clearPaint.setStrokeWidth(FINGER);

        //画背景
        drawBackGround();

    }

    /**
     * 绘制背景，背景包括背景图片和中奖信息
     */
    private void drawBackGround(){
        Bitmap bmpBackGround = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo);
        //从资源中读取的background不可修改，复制出一张可修改的图片
        Bitmap bmp = bmpBackGround.copy(Bitmap.Config.ARGB_8888, true);
        //在图片上画中奖信息
        Canvas cvsBackGroud = new Canvas(bmp);
        //计算出文字所占的区域，将文字放在正中间
        String text = PRIZE[getPrizeIndex()];
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int x = (bmp.getWidth() - rect.width()) / 2;
        int y = (bmp.getHeight() - rect.height()) / 2;
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
        mPaint.setShadowLayer(10, 0, 0, Color.GREEN);
        cvsBackGroud.drawText(text, x, y, mPaint);

        mPaint.setShadowLayer(0, 0, 0, Color.YELLOW);

        //画背景
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            this.setBackground(new BitmapDrawable(getResources(), bmp));
        }else{
            this.setBackgroundDrawable(new BitmapDrawable(bmp));
        }



    }

    private int getPrizeIndex(){
        return random.nextInt(PRIZE.length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bmpBuffer, 0, 0, mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化缓冲区
        bmpBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        cvsBuffer = new Canvas(bmpBuffer);
        //为缓冲区蒙上一层灰色
        cvsBuffer.drawColor(Color.parseColor("#FF808080"));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                currentX = x;
                currentY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                cvsBuffer.drawLine(currentX, currentY, x, y, clearPaint);
                invalidate();
                currentX = x;
                currentY = y;
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }
}
