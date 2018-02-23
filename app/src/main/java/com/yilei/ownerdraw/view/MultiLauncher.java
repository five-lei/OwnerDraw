package com.yilei.ownerdraw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by 易磊 on 2018/2/5.
 *
 * 第六步：响应用户手指的按下、移动和松开事件，这是整个滑动的关键，特别是松开后，要
 * 判断滚屏还是回滚。为了支持上一屏和下一屏，需要辨别手指滑动的方向，VelocityTracker 类可
 * 以获取 x 方向的速率，其正值代表向左滑动，负值代表向右滑动。如果 x 方向的速率在[-
 * SNAP_VELOCITY，SNAP_VELOCITY]之间，则要根据用户滑动的距离（滑动距离是否超过一屏的1/2）
 * 决定是要继续滚屏还是回滚到初始状态。
 */

public class MultiLauncher extends ViewGroup{
    private Scroller mScroller;
    private int touchSlop = 0;//最小滑动距离，超过了，才认为开始滑动
    private static final int TOUCH_STATE_STOP = 0x001;//停止状态
    private static final int TOUCH_STATE_FLING = 0x002;//滑动状态
    private int touchState = TOUCH_STATE_STOP;
    private float lastionMotionX = 0;//上次触摸屏的x位置

    private int curScreen;//当前屏
    private VelocityTracker velocityTracker;//速率跟踪器
    private static final int SNAP_VELOCITY = 500; // X轴速度基值，大于该值时进行切换

    private static final String TAG = "MultiLauncher";

    public MultiLauncher(Context context) {
        this(context, null);
    }

    public MultiLauncher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiLauncher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        //通过 ViewConfiguration.get(context).getScaledTouchSlop()可以获取到当前手机上默认的最小滑动距离
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 定位子组件的位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int n = this.getChildCount();
        int w = (r - l) / n;//容器宽度等于组件右走标点r - 起始坐标点l / 容器子控件的个数
        int h = b - t;//容器高度

        for (int i = 0; i < n; i++){
            View child = getChildAt(i);
            int left = i * w;
            int top = 0;
            int right = (i+1) * w;
            int bottom = h;
            child.layout(left, top, right, bottom);
        }

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
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //首先测量子控件
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 测量组件宽度
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec){
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        if(mode == MeasureSpec.AT_MOST){
            throw new  IllegalStateException("Must not be MeasureSpec.AT_MOST");
        }else{
            width = size;
        }

        //容器的宽度是屏幕的n倍，n是容器中子元素的个数
        return width * this.getChildCount();
    }

    /**
     * 测量组件高度
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec){
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;

        if(mode == MeasureSpec.AT_MOST){
            throw new IllegalStateException("Must not be MeasureSpec.AT_MOST");
        }else{
            height = size;
        }

        return height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //追踪touch事件(flinging事件和其他手势事件)的速率，使用obtain()方法初始化实例，
        //然后用addMovement(MotionEvent event)方法将你接收到的event加入到VelocityTracker类实例中
        //当你使用速率时使用computeCurrentVelocity(int)初始化速率的单位，并获得当前事件的速率
        //然后用getXVelocity()或getYVelocity()获得横向或纵向的速率
        //x方向上的速率 其正值代表向左滑动，负值代表向右滑动
        if(velocityTracker == null){
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        super.onTouchEvent(event);

        int action = event.getAction();
        int x = (int) event.getX();
//        int y = (int) event.getY();
        if(action == MotionEvent.ACTION_MOVE  && touchState == TOUCH_STATE_STOP )
            return true;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(mScroller != null && !mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                lastionMotionX = x;
                touchState = mScroller.isFinished() ? TOUCH_STATE_STOP : TOUCH_STATE_FLING;
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                //public static int abs(int a)
                //返回 int 值的绝对值。如果参数为非负数，则返回该参数。如果参数为负数，则返回该参数的相反数。
                //注意，如果参数等于 Integer.MIN_VALUE 的值（即能够表示的最小负 int 值），那么结果与该值相同且为负。
                //如下代码：
                //int a=Integer.MIN_VALUE;
                //System.out.println(a);//-2147483648
                //int num=Math.abs(a);
                //System.out.println(num);//-2147483648
                //因为最大值为2147483647，而绝对值为2147483648已经溢出，+1后变为最小值
                final int dxs = (int) Math.abs(x - lastionMotionX);
                if(dxs > touchSlop){
                    //大于则认为是滑动
                    touchState = TOUCH_STATE_FLING;
                }
                //随手指滚动
                int dx = (int) (lastionMotionX -x);
                scrollBy(dx, 0);
                lastionMotionX = x;
                break;
            case MotionEvent.ACTION_CANCEL:
                touchState = TOUCH_STATE_STOP;
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = this.velocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                //获得x轴横向的速率
                int velocityX = (int) velocityTracker.getXVelocity();
                if(velocityX > SNAP_VELOCITY  &&  curScreen > 0){
                    moveToPrevious();
                }else if(velocityX < -SNAP_VELOCITY  && curScreen < (getChildCount()-1)){
                    moveToNext();
                }else{
                    moveToDestination();
                }

                if(velocityTracker != null){
                    this.velocityTracker.clear();
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                touchState = TOUCH_STATE_STOP;
                break;
            default:
                break;
        }

        return true;
    }

    public void moveToScreen(int whichScreen){
        Log.i(TAG, "moveToScreen");
        curScreen = whichScreen;
        Log.i(TAG, "curScreen:"+curScreen);
        //因为curScreen是从0开始，如果大于getChildCount() - 1则划过了右边界；
        if(curScreen > getChildCount()-1)
            curScreen = getChildCount() - 1;

        if(curScreen < 0 ) curScreen = 0;
        //滑过的x距离
        int scrollX = getScrollX();
        //每一屏的宽度
        int splitWidth = getWidth() / getChildCount();
        //要移动的距离
        int dx = curScreen * splitWidth - scrollX;
        Log.i(TAG, "dx:"+dx);
        mScroller.startScroll(scrollX, getScrollY(), dx, 0, Math.abs(dx));
        invalidate();
    }

    public void moveToDestination(){
        Log.i(TAG, "moveToDestination");
        //每一屏的宽度
        int splitWidth = getWidth() / getChildCount();
        //判断是回滚还是进入下一分屏
        int toScreen = (getScrollX() + splitWidth/2) / splitWidth;
        Log.i(TAG, "toScreen:"+toScreen);
        //移动到目标分屏
        moveToScreen(toScreen);
    }

    /**
     * 滚动到下一屏
     */
    public void moveToNext(){
        moveToScreen(curScreen +1);
    }

    /**
     * 滚动到上一屏
     */
    public void moveToPrevious(){
        moveToScreen(curScreen -1);
    }



}
