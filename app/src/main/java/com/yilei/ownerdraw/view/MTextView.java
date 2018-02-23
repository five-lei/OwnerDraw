package com.yilei.ownerdraw.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.yilei.ownerdraw.R;

/**
 * Created by yilei on 2018/2/23.
 */

public class MTextView extends View{
    private Paint mPaint;
    private String text;
    private int textSize;
    private int textColor;
    private Rect mBound;

    public MTextView(Context context) {
        this(context, null);
    }

    public MTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MTextView, 0, 0 );
        textColor = typedArray.getColor(R.styleable.MTextView_text_colors, 0);
        textSize = typedArray.getDimensionPixelSize(R.styleable.MTextView_text_sizes, 0);
        text = typedArray.getString(R.styleable.MTextView_texts);
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mBound);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseLine = getMeasuredHeight() / 2 + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
        canvas.drawText(text, (getWidth()/2) - (mBound.width()/2), baseLine, mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    private int measureWidth(int widthMeasureSpec){
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;

        if(mode == MeasureSpec.EXACTLY){
            //如果是math_parent或确定尺寸
            width = size;
        }else if(mode == MeasureSpec.AT_MOST){
            //如果是wrap_parent
            width = getPaddingLeft() + mBound.width() + getPaddingRight();
        }

        return width;

    }

    private int measureHeight(int heightMeasureSpec){
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        int height = 0;
        if(mode == MeasureSpec.EXACTLY){
            //如果是math_parent或确定尺寸
            height = size;
        }else if(mode == MeasureSpec.AT_MOST){
            //如果是wrap_parent
            height = getPaddingTop() + mBound.height() + getPaddingBottom();
        }

        return height;
    }
}
