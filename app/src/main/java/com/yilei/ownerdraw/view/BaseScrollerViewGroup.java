package com.yilei.ownerdraw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Scroller;

/**
 * Created by 易磊 on 2018/2/5.
 */

public class BaseScrollerViewGroup extends ViewGroup{
    private Scroller mScroller;//滚动器
    private Button btnAndroid;//按钮

    public BaseScrollerViewGroup(Context context) {
        super(context);
    }

    public BaseScrollerViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseScrollerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btnAndroid = new Button(context);
        btnAndroid.setText("Android");
        btnAndroid.setLayoutParams(layoutParams);
        this.addView(btnAndroid);

    }

    /**
     * 定位
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        btnAndroid.layout(10, 10, btnAndroid.getMeasuredWidth()+10, btnAndroid.getMeasuredHeight()+10);

    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST)
            throw new IllegalStateException("Must be MeasureSpec.EXACTLY");
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    /**
     *实现平滑滚动
     */
    @Override
    public void computeScroll() {
        //computeScrollOffset() 计算滚动偏移量，必调方法之一
        // 主要负责计算 currX 和 currY 两个值，其返回值为true 表示滚动尚未完成，为 false 表示滚动已结束；
        if(mScroller.computeScrollOffset()){
            //设置容器内组件的新位置
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //重绘以刷新产生动画
            postInvalidate();

        }
    }

    /**
     * 开始滚动
     */
    public void start(){
        //从当前位置开始滚动，x轴向右滚动900， y轴就是，，也就是水平向右滚动
        //滚动时间10000
        mScroller.startScroll(this.getScrollX(), this.getScrollY(), -900, 0, 10000);
        //重绘
        postInvalidate();

    }

    /**
     * 结束滚动,到达目的地
     */
    public void abort(){
        //mScroller.forceFinished(boolean isFinished)  强制结束滚动，currX，currY即为当前坐标
        //mScroller.abortAnimation(); 与forceFinished(true)相似,停止滚动，但是currX，currY即为终点坐标
        mScroller.abortAnimation();
    }
}
