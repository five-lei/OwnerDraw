package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 易磊 on 2018/1/29.
 * 自定义流式布局
 */

public class FlowLayout extends ViewGroup{

    private static final String TAG = "FlowLayout";

    public FlowLayout(Context context){
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(this.getContext(), attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int n = getChildCount();//获取子组件的总数;
        int maxViewHeight = 0;//当前行的子组件最大高度
        int maxLineWidth = 0; //当前行的子组件的总宽度

        int totalHeight = 0; //累计高度
        int width = getMeasuredWidth();//容器宽度

        int wMargin = 0;//当前子组件的左右margin
        int hMargin = 0;//当前子组件的上下margin
        for(int i = 0; i < n ; i++){
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            wMargin = layoutParams.leftMargin+layoutParams.rightMargin;
            hMargin = layoutParams.topMargin+ layoutParams.bottomMargin;
            //判断是否需要换行, (当前已占总宽度+当前子组件的宽度+左右margin是否大于容器的宽度)
            if(maxLineWidth + getChildAt(i).getMeasuredWidth() + wMargin
                    > width - getPaddingLeft() - getPaddingRight()){
                //换行后累计已显示的行的总高度
                totalHeight += maxViewHeight;
                Log.i(TAG, "totalHeight:"+ totalHeight+"maxLineWidth:"+maxLineWidth+"width:"+width);
                //另起一行，当前行的最大高度和总宽度重置为0
                maxLineWidth = 0;
                maxViewHeight = 0;
            }

            layoutChild(child, maxLineWidth+layoutParams.leftMargin,
                    totalHeight+layoutParams.topMargin,
                    maxLineWidth+child.getMeasuredWidth()+layoutParams.leftMargin,
                    totalHeight+child.getMeasuredHeight()+layoutParams.topMargin);
            //获取当前行的最高高度
            maxViewHeight = Math.max(maxViewHeight, child.getMeasuredHeight()+hMargin);
            //累加当前行的总宽度
            maxLineWidth = maxLineWidth + child.getMeasuredWidth() + wMargin;

        }

    }

    /**
     * 定位子组件，方法内考虑padding和margin
     * @param child
     * @param l
     * @param t
     * @param r
     * @param b
     */
    private void layoutChild(View child, int l, int t, int r, int b){
        Log.i(TAG, child.getTag()+"left:"+l+"top:"+t+"right:"+r+"bottom:"+b);
        child.layout(l+getPaddingLeft(),
                     t+getPaddingTop(),
                     r+getPaddingLeft(),
                     b+getPaddingTop());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 测量容器的宽度
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec){
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = 0;
        if(widthMode == MeasureSpec.EXACTLY){
            //如果是math_parent或是具体尺寸
            width = widthSize;
        }else if(widthMode == MeasureSpec.AT_MOST){
            //如果是wrap_parent

            int n = getChildCount();
            int childrenWidth = 0;//子组件宽度总和

            int wMargin = 0;

            for(int i = 0; i < n; i++){
                View child = getChildAt(i);
                MarginLayoutParams layoutParams = (MarginLayoutParams) getChildAt(i).getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                //单个子组件的宽度不能超过容器的宽度
                if(childWidth > widthSize){
                    throw new IllegalStateException("Sub view is too large!");
                }
                childrenWidth += childWidth;
                wMargin += (layoutParams.leftMargin + layoutParams.rightMargin);
            }

            Log.i(TAG, "widthSize:"+widthSize+"childrenWidth:"+childrenWidth);

            if(childrenWidth+wMargin > widthSize){
                //如果当前子组件的宽度之和比当前容器的宽度大，则使用容器宽度
                width = widthSize;
            }else{
                //反之则使用组件宽度之和
                width = childrenWidth+wMargin;
            }
            width += this.getPaddingLeft()+this.getPaddingRight();
        }

        return width;
    }

    /**
     * 测量容器的高度
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec){
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;
        if(heightMode == MeasureSpec.EXACTLY){
            //如果是精确的尺寸或者是math_parent
            height = heightSize;
        }else if(heightMode == MeasureSpec.AT_MOST){
            //如果是wrap_parent
            int width = getMeasuredWidth();
            int n = getChildCount();
            int maxViewHeight = 0;//当前行的最大高度
            int maxLineWidth = 0;//当前行的子组件的总宽度


            int maxHMargin = 0;

            for(int i = 0; i < n; i++){
                View child = getChildAt(i);
                MarginLayoutParams layoutParams = (MarginLayoutParams) getChildAt(i).getLayoutParams();
                //子组件上下margin之和
                int hMargin = layoutParams.topMargin + layoutParams.bottomMargin;
                //子组件左右margin之和
                int wMargin = layoutParams.leftMargin + layoutParams.rightMargin;
                //取每个子组件当中上下margin之和最大的
                maxHMargin = Math.max(maxHMargin, hMargin);
                //累加当前行的总宽度
                maxLineWidth = maxLineWidth + child.getMeasuredWidth() + wMargin;
                //去当前行的最大高度，因考虑margin，所以应该和child.getMeasuredHeight()+maxHMargin比较
                maxViewHeight = Math.max(maxViewHeight, child.getMeasuredHeight()+maxHMargin);


                //预测是否需要换行
                if(i < n-1 && maxLineWidth + getChildAt(i+1).getMeasuredWidth() + wMargin > width - getPaddingLeft() - getPaddingRight()){
                    //如果当前行子组件的总宽度大于容器宽度，则需要换行
                    height += maxViewHeight;
                    //重置当前行的最大高度和子组件宽度之和
                    maxViewHeight = 0;
                    maxLineWidth = 0;
                    maxHMargin = 0;
                }else if(i == n-1){
                    //当子组件是最后一个
                    height += maxViewHeight;
                }
            }
            height += getPaddingTop() + getPaddingBottom();
        }

        return height;
    }
}
