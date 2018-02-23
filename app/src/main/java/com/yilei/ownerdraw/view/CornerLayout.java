package com.yilei.ownerdraw.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yilei.ownerdraw.R;

import java.security.PublicKey;

/**
 * Created by 易磊 on 2018/1/25.
 * 自定义容器
 */

public class CornerLayout extends ViewGroup{


    public CornerLayout(Context context) {
        this(context, null);
    }

    public CornerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PositionLayoutParams(this.getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new PositionLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new PositionLayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    public static class PositionLayoutParams extends MarginLayoutParams{

        public static final int LEFT_TOP = 0;
        public static final int RIGHT_TOP = 1;
        public static final int LEFT_BOTTOM = 2;
        public static final int RIGHT_BOTTOM = 3;
        public static final int NONE = -1;
        public int position;

        public PositionLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //读取layout_position属性
            TypedArray t = c.obtainStyledAttributes(attrs, R.styleable.CornerLayout);
            position = t.getInt(R.styleable.CornerLayout_layout_position, NONE);
            t.recycle();
        }

        public PositionLayoutParams(LayoutParams source) {
            super(source);
        }

        public PositionLayoutParams(int width, int height) {
            super(width, height);
        }

        public PositionLayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //设置子组件的位置和大小
        for(int i = 0; i < getChildCount(); i++){
            View child = getChildAt(i);
            PositionLayoutParams layoutParams = (PositionLayoutParams) getChildAt(i).getLayoutParams();
            int position = layoutParams.position;

            if(i == 0 && position == PositionLayoutParams.NONE || layoutParams.position == PositionLayoutParams.LEFT_TOP){
                //定位到左上角
                child.layout(getPaddingLeft() + layoutParams.leftMargin,
                             getPaddingTop() + layoutParams.topMargin,
                             getPaddingLeft() + layoutParams.leftMargin + child.getMeasuredWidth(),
                             child.getMeasuredHeight() + getPaddingTop() + layoutParams.topMargin);
            }else if(i == 1 && position == PositionLayoutParams.NONE || layoutParams.position == PositionLayoutParams.RIGHT_TOP){
                //定位到右上角
                child.layout(getMeasuredWidth() - child.getMeasuredWidth()- getPaddingRight() - layoutParams.rightMargin,
                             getPaddingTop() + layoutParams.topMargin,
                             getMeasuredWidth() - getPaddingRight() - layoutParams.rightMargin,
                             child.getMeasuredHeight() + getPaddingTop() + layoutParams.topMargin);
            }else if(i == 2 && position == PositionLayoutParams.NONE || layoutParams.position == PositionLayoutParams.LEFT_BOTTOM){
                //定位到左下角
                child.layout(getPaddingLeft() + layoutParams.leftMargin,
                             getMeasuredHeight() - child.getMeasuredHeight()-getPaddingBottom() - layoutParams.bottomMargin,
                             getPaddingLeft()+child.getMeasuredWidth()+layoutParams.leftMargin,
                             getMeasuredHeight()- getPaddingBottom() - layoutParams.bottomMargin);
            }else if(i == 3 && position == PositionLayoutParams.NONE || layoutParams.position == PositionLayoutParams.RIGHT_BOTTOM){
                //定位到右下角
                child.layout(getMeasuredWidth()- child.getMeasuredWidth()- getPaddingRight() - layoutParams.rightMargin,
                             getMeasuredHeight() - child.getMeasuredHeight()-getPaddingBottom()- layoutParams.bottomMargin,
                             getMeasuredWidth()-getPaddingRight()- layoutParams.rightMargin,
                             getMeasuredHeight() -getPaddingBottom() - layoutParams.bottomMargin);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //先测量所有子组件的大小
        this.measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量容器自身的大小
        int width = this.measureWidth(widthMeasureSpec);
        int height = this.measureHeight(heightMeasureSpec);
        //应用尺寸
        setMeasuredDimension(width, height);

    }

    /**
     * 测量容器的宽度
     * @param widthMeasureSpec
     */
    private int measureWidth(int widthMeasureSpec){
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = 0;

        if(widthMode == MeasureSpec.EXACTLY){
            //math_parent或是具体值
            width = widthSize;
        }else if(widthMode == MeasureSpec.AT_MOST){
            int aWidth = 0;
            int bWidth = 0;
            int cWidth = 0;
            int dWidth = 0;

            int aWMargin = 0;//a组件左右margin
            int bWMargin = 0;//b组件左右margin
            int cWMargin = 0;//c组件左右margin
            int dWMargin = 0;//d组件左右margin

            for(int i = 0; i < getChildCount(); i++){
                PositionLayoutParams layoutParams = (PositionLayoutParams) getChildAt(i).getLayoutParams();
                if(i == 0){
                    aWidth = getChildAt(i).getMeasuredWidth();
                    aWMargin = layoutParams.leftMargin + layoutParams.rightMargin;
                }else if(i == 1){
                    bWidth = getChildAt(i).getMeasuredWidth();
                    bWMargin = layoutParams.leftMargin + layoutParams.rightMargin;
                }else if(i == 2){
                    cWidth = getChildAt(i).getMeasuredWidth();
                    cWMargin = layoutParams.leftMargin + layoutParams.rightMargin;
                }else if(i == 3){
                    dWidth = getChildAt(i).getMeasuredWidth();
                    dWMargin = layoutParams.leftMargin + layoutParams.rightMargin;
                }
            }
            //取ac中宽度最宽的加上bd中宽度最宽的 + 左右外边距 + 左右内边距
            width = getPaddingLeft()
                    + Math.max(aWMargin, cWMargin)
                    + Math.max(aWidth, cWidth)
                    + Math.max(bWidth, dWidth)
                    + Math.max(bWMargin, dWMargin)
                    + getPaddingRight();
        }
        return width;
    }

    /**
     * 测量容器的高度
     * @param heightMeasureSpec
     */
    private int measureHeight(int heightMeasureSpec){
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;

        if(heightMode == MeasureSpec.EXACTLY){
            //mach_parent或固定值
            height = heightSize;
        }else if(heightMode == MeasureSpec.AT_MOST){
            //wrap_content
            int aHeight = 0;
            int bHeight = 0;
            int cHeight = 0;
            int dHeight = 0;

            int aHMargin = 0;//a上下margin
            int bHMargin = 0;//b上下margin
            int cHMargin = 0;//c上下margin
            int dHMargin = 0;//d上下margin

            for(int i = 0; i < getChildCount(); i++){
                PositionLayoutParams layoutParams = (PositionLayoutParams) getChildAt(i).getLayoutParams();
                if(i == 0){
                    //左上角
                    aHeight = getChildAt(i).getMeasuredHeight();
                    aHMargin = layoutParams.topMargin + layoutParams.bottomMargin;
                }else if(i == 1){
                    //右上角
                    bHeight = getChildAt(i).getMeasuredHeight();
                    bHMargin = layoutParams.topMargin + layoutParams.bottomMargin;
                }else if(i == 2){
                    //左下角
                    cHeight = getChildAt(i).getMeasuredHeight();
                    cHMargin = layoutParams.topMargin + layoutParams.bottomMargin;
                }else if(i == 3){
                    //右下角
                    dHeight = getChildAt(i).getMeasuredHeight();
                    dHMargin = layoutParams.topMargin + layoutParams.bottomMargin;
                }

            }

            //取ab中高度最高的叫上cd中高度最高的 + 上下外边距 + 上下内边距
            height = Math.max(aHeight, bHeight)
                    + Math.max(cHeight, dHeight)
                    + Math.max(aHMargin, bHMargin)
                    + Math.max(cHMargin, dHMargin)
                    + getPaddingBottom()
                    + getPaddingTop();
        }
        return height;
    }

}
