package com.yilei.ownerdraw.view.Handtouch;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by 易磊 on 2018/1/22.
 */

public class AttributesTool {
    /**绘图颜色*/
    private int color;
    /**边线的宽度*/
    private int borderWidth;
    /**是否填充，默认是空心*/
    private boolean fill;

    private static AttributesTool self;
    private static Paint mPaint;

    /**
     * 将构造方法私有，目的为了防止重复创建实例
     */
    private AttributesTool(){
        reset();
    }

    /**
     * 对外部提供对象
     * @return
     */
    public static AttributesTool getInstance(){
        if (self == null){
            self = new AttributesTool();
        }
        return self;
    }

    /**
     * 将当前的绘图画笔属性转换成Paint对象
     * @return
     */
    public Paint getPaint(){
        if(mPaint == null){
            mPaint = new Paint();
        }
        mPaint.setAntiAlias(true);
        mPaint.setColor(this.color);
        mPaint.setStrokeWidth(this.borderWidth);
        mPaint.setStyle(this.fill ? Paint.Style.FILL : Paint.Style.STROKE);
        mPaint.setTextSize(30);
        return mPaint;
    }

    /**
     * 重置画笔属性
     */
    public void reset(){
        this.color = Color.BLACK;
        this.borderWidth = 1;
        this.fill = false;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getBorderWidth(){
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth){
        this.borderWidth = borderWidth;
    }

    public boolean isFill(){
        return fill;
    }

    public void setFill(boolean fill){
        this.fill = fill;
    }




}
