package com.yilei.ownerdraw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yilei on 2018/1/19.
 */

public class RotateBoll extends View{
    private int x;//小球初始的x坐标
    private int y;//小球的y坐标
    private int radius;//小球的半径

    private int color;//小球的颜色

    private Paint mPaint;

    private boolean direction;//小球移动的方向


    public RotateBoll(Context context){
        this(context, null);
    }

    public RotateBoll(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateBoll(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RotateBoll);
        y = typedArray.getInt(R.styleable.RotateBoll_boll_y, 0);
        radius = typedArray.getInt(R.styleable.RotateBoll_boll_radius, 0);
        color = typedArray.getColor(R.styleable.RotateBoll_boll_color, Color.GREEN);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        x = radius;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(x , y, radius, mPaint);

        int width = this.getMeasuredWidth();
        if(x <= radius){
            direction = true;
        }

        if(x >= width - radius){
            direction = false;
        }

        x = direction ? x+15 : x-15;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.v("RotateBoll", "宽的模式："+widthMode);
        Log.v("RotateBoll", "高的模式："+heightMode);
        Log.v("RotateBoll", "宽的尺寸："+widthSize);
        Log.v("RotateBoll", "高的尺寸："+heightSize);

        int width;
        int height;
        if(widthMode == MeasureSpec.EXACTLY){
            //如果是match_parent或固定的值
            width = widthSize;
        }else{
            //如果是wrap_parent，我们需要得到控件的具体尺寸
            float boll_width = radius * 2;
            //控件的宽度就是圆的直径加上两边的内边距
            width = (int) (boll_width);
            Log.v("RotateBoll", "boll_width："+width);
        }

        if(heightMode == MeasureSpec.EXACTLY){
            //如果是match_parent或固定的值
            height = heightSize;
        }else{
            //如果是wrap_parent，我们需要得到控件的具体尺寸
            float boll_height = radius * 2;
            //控件的高度就是圆的直径加上上下的内边距
            height = (int) (boll_height + getPaddingBottom() +getPaddingTop());
        }

        setMeasuredDimension(width, height);
    }

    public void setBollX(int x){
        this.x = x;
    }

    public int getBollX(){
        return x;
    }

    public void setBollY(int y){
        this.y = y;
    }

    public int getBollY(){
        return y;
    }

    public void setBollRadius(int radius){
        this.radius = radius;
    }

    public int getBollRadius(){
        return radius;
    }


}
