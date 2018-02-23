package com.yilei.ownerdraw.view.KLineChart;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

/**
 * Created by 易磊 on 2018/2/6.
 * K线滑动和缩放的基类
 */

public abstract class ScrollAndScaleView extends RelativeLayout implements
        GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener{

    protected int mScrollX = 0;
    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector mScaleDetector;
    private OverScroller mScroller;//升级版的Scroller

    private boolean touch = false;//是否在触摸,默认否false
    protected boolean isLongPress = false;//是否长按
    private boolean mMultipleTouch = false;//是否是多指触摸
    private boolean mScrollEnable = true;//是否可以滚动设置
    private boolean mScaleEnable = true;//是否支持缩放

    protected float mScaleX = 1;
    protected float mScaleXMax = 2f;//最大缩放比
    protected float mScaleXMin = 0.5f;//最小缩放比

    public ScrollAndScaleView(Context context) {
        super(context);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化触摸手势、缩放手势和OverScroller;
     */
    private void init(){
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mScroller = new OverScroller(getContext());
    }

    /**
     * 平滑移动
     */
    @Override
    public void computeScroll() {
        //返回true表示滚动尚未完成，false表示已经完成
        if(mScroller.computeScrollOffset()){
            if(!isTouch()){
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }else{
                mScroller.forceFinished(true);
            }

        }
    }

    @Override
    public void scrollTo(int x, int y) {
        //如果设置了不能滑动
        if(!isScrollEnable()){
            //强制结束滚动， currX, currY即为当前坐标
            mScroller.forceFinished(true);
            return;
        }

        int oldScrollX = mScrollX;
        mScrollX = x;
        if(mScrollX < getMinScrollX()){
            //如果比位移的最小值还小，则取最小值
            mScrollX = getMinScrollX();
            onRightSide();
            mScroller.forceFinished(true);
        }else if(mScrollX > getMaxScrollX()){
            //如果比位移最大值还大，则取最大值
            mScrollX = getMaxScrollX();
            onLeftSide();
            mScroller.forceFinished(true);
        }
        onScrollChanged(mScrollX, 0, oldScrollX, 0);
        invalidate();
    }

    @Override
    public void scrollBy(int x, int y) {
        scrollTo(mScrollX - Math.round(x / mScaleX), 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount() == 1){
                    //长按之后移动
                    if(isLongPress){
                        onLongPress(event);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isLongPress = false;
                touch = false;
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                touch = false;
                invalidate();
                break;
        }
        mMultipleTouch = event.getPointerCount()>1;
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 触摸手势 手指按下触摸屏的那个瞬间回调
     * @param e
     * @return
     */
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    /**
     * 手指按下滑动而不是长按的时候回调
     * @param e
     */
    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     * 手指按下迅速松开的那个瞬间回调
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 手指在触摸屏滑动的时候回调
     * @param e1 The first down motion event that started the scrolling.
     * @param e2 The move motion event that triggered the current onScroll.
     * @param distanceX The distance along the X axis that has been scrolled since the last
     *              call to onScroll. This is NOT the distance between {@code e1}
     *              and {@code e2}.
     * @param distanceY he distance along the Y axis that has been scrolled since the last
     *              call to onScroll. This is NOT the distance between {@code e1}
     *              and {@code e2}.
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(!isLongPress && !ismMultipleTouch()){
            scrollBy(Math.round(distanceX), 0);
            return true;
        }
        return false;
    }

    /**
     * 手指长按触摸屏的时候回调
     * @param e
     */
    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
    }

    /**
     * 手指迅速移动并松开的时候回调
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(!isTouch() && isScrollEnable()){
            mScroller.fling(mScrollX, 0,
                    Math.round(velocityX / mScaleX), 0,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if(!isScaleEnable()){
            return false;
        }
        float oldScale = mScaleX;
        mScaleX *= detector.getScaleFactor();//获取当前的缩放比
        if(mScaleX < mScaleXMin){
            mScaleX = mScaleXMin;
        }else if(mScaleX > mScaleXMax){
            mScaleX = mScaleXMax;
        }else{
            onScaleChanged(mScaleX, oldScale);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //一定要返回true才会进入onScale()函数
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    protected void onScaleChanged(float scale,float oldScale)
    {
        invalidate();
    }

    /**
     * 是否在触摸中
     * @return
     */
    public boolean isTouch(){
        return touch;
    }

    /**
     * 是否是多指触摸
     * @return
     */
    public boolean ismMultipleTouch(){
        return mMultipleTouch;
    }

    /**
     * 检查是否没有滑动到最小值和滑动过了最大值
     */
    public void checkAndFixScrollX(){
        if(mScrollX < getMinScrollX()){
            mScrollX = getMinScrollX();
            mScroller.forceFinished(true);
        }else if(mScrollX > getMaxScrollX()){
            mScrollX = getMaxScrollX();
            mScroller.forceFinished(true);
        }
    }

    /**
     * 是否设置滚动
     * @return
     */
    public boolean isScrollEnable(){
        return mScrollEnable;
    }

    /**
     * 是否设置缩放
     * @return
     */
    public boolean isScaleEnable(){
        return mScaleEnable;
    }

    /**
     * 设置是否可以滚动
     * @param scrollEnable
     */
    public void setScrollEnable(boolean scrollEnable){
        this.mScrollEnable = scrollEnable;
    }

    /**
     * 设置是否可以缩放
     * @param scaleEnable
     */
    public void setScaleEnable(boolean scaleEnable){
        this.mScaleEnable = scaleEnable;
    }

    /**
     * 设置scrollX
     * @param scrollX
     */
    public void setScrollX(int scrollX){
        this.mScrollX = scrollX;
        scrollTo(scrollX, 0);
    }

    /**
     * 获取x轴缩放比
     * @return
     */
    public float getScaleX(){
        return mScaleX;
    }

    /**
     * 获取x轴最小缩放比
     * @return
     */
    public float getScaleXMin(){
        return mScaleXMin;
    }

    /**
     * 获取x轴最大缩放比
     * @return
     */
    public float getScaleXMax(){
        return mScaleXMin;
    }

    /**
     * 设置x轴最小缩放比
     * @param scaleXMin
     */
    public void setScaleXMin(float scaleXMin){
        this.mScaleXMin = scaleXMin;
    }

    /**
     * 设置x轴最大缩放比
     * @param scaleXMax
     */
    public void setScaleXMax(float scaleXMax){
        this.mScaleXMax = scaleXMax;
    }

    /**
     * 获取位移的最小值
     * @return
     */
    public abstract int getMinScrollX();

    /**
     * 获取位移的最大值
     * @return
     */
    public abstract int getMaxScrollX();
    /**
     * 滑到了最左边
     */
    public abstract void onLeftSide();

    /**
     * 滑到了最右边
     */
    public abstract void onRightSide();
}
