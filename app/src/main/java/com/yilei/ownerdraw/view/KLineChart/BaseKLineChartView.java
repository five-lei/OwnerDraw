package com.yilei.ownerdraw.view.KLineChart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yilei.ownerdraw.R;
import com.yilei.ownerdraw.view.KLineChart.base.IAdapter;
import com.yilei.ownerdraw.view.KLineChart.base.IChartDraw;
import com.yilei.ownerdraw.view.KLineChart.base.IDateTimeFormatter;
import com.yilei.ownerdraw.view.KLineChart.base.IValueFormatter;
import com.yilei.ownerdraw.view.KLineChart.base.OnSelectedChangedListener;
import com.yilei.ownerdraw.view.KLineChart.entityImpl.KLineImpl;
import com.yilei.ownerdraw.view.KLineChart.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 易磊 on 2018/2/6.
 */

public abstract class BaseKLineChartView extends ScrollAndScaleView{
    private KChartTabView mKChartTabView;
    private int mTopPadding;
    private int mBottomPadding;

    private int mWidth = 0;
    private Rect mKlineRect;//k线矩形
    private Rect mTabRect;//tab切换矩形
    private Rect mChildRect;

    private Paint mBackGroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//容器背景画笔
    private Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//容器网格画笔
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//十字线画笔

    private int mItemCount;
    private int mSelectedIndex;
    private int mStartIndex = 0;
    private int mStopIndex = 0;
    private int mGridRows = 4;//k线网格的行数
    private int mGridColumns = 4;//k线网格的列数
    private float mPointWidth = 6;//每个点的宽度
    private float mDataLen = 0;//数据长度 = 点的个数 * 点的宽度
    private float mTranslateX = Float.MIN_VALUE;
    private float mKlineMaxValue = Float.MAX_VALUE;//k线的最大值
    private float mKlineMinValue = Float.MIN_VALUE;//k线的最小值
    private float mChildMaxValue = Float.MAX_VALUE;//底部图表的最大值
    private float mChildMinValue = Float.MIN_VALUE;//底部图表的最小值
    private float mChildScaleY = 1;
    private float mMainScaleY = 1;

    private IChartDraw mMainDraw;
    private IChartDraw mChildDraw;
    private List<IChartDraw> mChildDraws = new ArrayList<>();
    private int mChildDrawPosition = 0;//子视图集合的下标
    private IAdapter mAdapter;
    private IValueFormatter mValueFormatter;
    private IDateTimeFormatter mIDateTimeFormatter;
    private ValueAnimator mAnimator;
    private long mAnimationDuration = 500;

    private OnSelectedChangedListener onSelectedChangedListener = null;//选中点变化接口

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }
    };

    public BaseKLineChartView(Context context) {
        super(context);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //ViewGroup默认情况下，出于性能考虑，会被设置成WILL_NOT_DRAW，这样，onDraw就不会被执行了
        //构造函数中，调用setWillNotDraw（false），去掉其WILL_NOT_DRAW flag
        //onDraw()方法不被执行的解决方法
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mKChartTabView = new KChartTabView(getContext());
        mTopPadding = (int) getResources().getDimension(R.dimen.chart_top_padding);
        mBottomPadding = (int) getResources().getDimension(R.dimen.chart_bottom_padding);

        addView(mKChartTabView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mKChartTabView.setOnTabSelectListener(new KChartTabView.TabSelectListener() {
            @Override
            public void onTabSelected(int position) {
                setChildDraw(position);
            }
        });

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        initRect(w, h);
        mKChartTabView.setTranslationY(mKlineRect.bottom);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        //保存当前的index
        int lastIndex = mSelectedIndex;
        //滑动之后重新计算index
        calculateSelectedX(e.getX());
        if(lastIndex != mSelectedIndex){
            onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        //重绘
        invalidate();

    }

    /**
     * 计算选中的x
     * @param x
     */
    private void calculateSelectedX(float x){
        mSelectedIndex =indexOfTranslateX(xToTranslateX(x));
        if(mSelectedIndex < mStartIndex){
            mSelectedIndex = mStartIndex;
        }

        if(mSelectedIndex > mStopIndex){
            mSelectedIndex = mStopIndex;
        }

    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        checkAndFixScrollX();
        setTranslateXFromScrollX(mScrollX);
        super.onScaleChanged(scale, oldScale);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
    }

    private void initRect(int width, int height){
        int kLineChildSpace = mKChartTabView.getMeasuredHeight();//获取tab的高度
        int displayHeight = height - mTopPadding - mBottomPadding - kLineChildSpace;
        int kLineHeight = (int) (displayHeight * 0.75f);//k线高度为整个组件高度的0.75
        int childHeight = (int) (displayHeight * 0.25);//底部视图为整个组件高度的0.25
        mKlineRect = new Rect(0, mTopPadding, width, kLineHeight + mTopPadding);//k线矩形
        mTabRect = new Rect(0, mKlineRect.bottom, width, mKlineRect.bottom+kLineChildSpace);//tab矩形
        mChildRect = new Rect(0, mTabRect.bottom, width, mTabRect.bottom+childHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackGroundPaint.getColor());
        if(mWidth == 0 || mKlineRect.height() == 0 || mItemCount == 0){
            return;
        }

        calculateValue();//计算当前的显示区域
        canvas.save();
        canvas.scale(1, 1);
        drawGrid(canvas);
        drawKline(canvas);
        drawText(canvas);
        drawValue(canvas, isLongPress ? mSelectedIndex : mStopIndex);
        canvas.restore();
    }

    /**
     * 画网格
     * @param canvas
     */
    private void drawGrid(Canvas canvas){
        //-----------------------上方k线图-------------------------
        //横向的grid
        int rowSpace = mKlineRect.height() / mGridRows;
        for (int i = 0; i <= mGridRows; i++){
            canvas.drawLine(0, i * rowSpace + mKlineRect.top, mWidth, i * rowSpace + mKlineRect.top, mGridPaint);
        }

        //-----------------------下方子图----------------------------
        canvas.drawLine(0, mChildRect.top, mWidth, mChildRect.top, mGridPaint);
        canvas.drawLine(0, mChildRect.bottom, mWidth, mChildRect.bottom, mGridPaint);

        //纵向的grid
        int columnSpace = mWidth / mGridColumns;
        for (int i = 0; i <= mGridColumns; i++){
            //上方k线图
            canvas.drawLine(i * columnSpace, mKlineRect.top , i * columnSpace, mKlineRect.bottom, mGridPaint);
            //下方子图
            canvas.drawLine(i * columnSpace, mChildRect.top, i * columnSpace, mChildRect.bottom, mGridPaint);
        }
    }

    /**
     * 画文字
     * @param canvas
     */
    private void drawText(Canvas canvas){
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.ascent - fm.descent;//文字高度
        //text baseLine计算公式 int baseline = height / 2 + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
        float baseLine = textHeight / 2 + (fm.descent - fm.ascent) / 2 - fm.descent;
        //---------------画上方k线图的值-----------------
        if(mMainDraw != null){
            //画蜡烛图左边的最大值和最小值
            canvas.drawText(formatValue(mKlineMaxValue), 0, baseLine + mKlineRect.top, mTextPaint);
            canvas.drawText(formatValue(mKlineMinValue), 0, mKlineRect.bottom - textHeight + baseLine, mTextPaint);
            float rowValue  = (mKlineMaxValue - mKlineMinValue) / mGridRows;//每行的值
            float rowSpace = mKlineRect.height() / mGridRows;//每行的高度
            for(int i = 1; i < mGridRows; i++){
                //画蜡烛图除了最大值和最小值得text
                String text = formatValue(rowValue * (mGridRows - i) + mKlineMinValue);
                canvas.drawText(text, 0, fixTextY(rowSpace * i + mKlineRect.top), mTextPaint);
            }
        }

        //--------------画下方子图的值---------------------
        if(mChildDraw != null){
            //画一屏左右两边的时间，当前屏的最大值和最小值
            canvas.drawText(mChildDraw.getValueFormatter().format(mChildMaxValue), 0 , mChildRect.top + baseLine, mTextPaint);
            canvas.drawText(mChildDraw.getValueFormatter().format(mChildMinValue), 0, mChildRect.bottom, mTextPaint);
        }

        //--------------画时间-----------------
        float columnSpace = mWidth / mGridColumns;
        float y = mChildRect.bottom + baseLine;

        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;
        for(int i = 1; i < mGridColumns; i++){
            float translateX = xToTranslateX(columnSpace * i);
            if(translateX >= startX && translateX <= stopX){
                //画除开左右两边中间的时间
                int index = indexOfTranslateX(translateX);
                String text = formatDateTime(mAdapter.getDate(index));
                canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTextPaint);
            }
        }

        float translateX = xToTranslateX(0);
//        if()


    }

    /**
     * 画值
     * @param canvas
     * @param position 某个点的值
     */
    private void drawValue(Canvas canvas, int position){

    }

    /**
     * 画k线图
     * @param canvas
     */
    private void drawKline(Canvas canvas){
        //保存之前的平移缩放
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(mScaleX, 1);
        for(int i = mStartIndex; i <= mStopIndex; i++){
            //当前点
            Object currentPoint = getItem(i);
            //当前点的x
            float currentPointX = getX(i);
            //上一个点
            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastPointX = i == 0 ? currentPointX : getX(i - 1);
            if(mMainDraw != null){
                //画上方k线 CandleDraw
                mMainDraw.drawTranslated(lastPoint, currentPoint, lastPointX, currentPointX, canvas, this, i);
            }

            if(mChildDraw != null){
                //根据点击的子视图类型画子视图
                mChildDraw.drawTranslated(lastPoint, currentPoint, lastPointX, currentPointX, canvas, this, i);
            }
        }

        //画选择线
        if(isLongPress()){
            KLineImpl point = (KLineImpl) getItem(mSelectedIndex);
            float x = getX(mSelectedIndex);
            float y = getMainY(point.getClosePrice());

            //画垂直线
            canvas.drawLine(x, mKlineRect.top, x, mKlineRect.bottom, mSelectedLinePaint);
            canvas.drawLine(x, mChildRect.top, x, mChildRect.bottom, mSelectedLinePaint);
            //画水平线
            canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mSelectedLinePaint);
        }

        //还原平移缩放
        canvas.restore();

    }

    /**
     * 在主区域画线
     * @param canvas
     * @param paint
     * @param startX
     * @param startValue
     * @param stopX
     * @param stopValue
     */
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue){
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }

    /**
     * 在子区域画线
     * @param canvas
     * @param paint 子视图实现类传过来的画笔
     * @param startX 开始点的x坐标
     * @param startValue 开始点的值
     * @param stopX 结束点的坐标
     * @param stopValue 结束点的值
     */
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue){
        canvas.drawLine(startX, getChildY(startValue), stopX, getChildY(stopValue), paint);
    }

    /**
     * 计算当前的显示区域
     */
    private void calculateValue(){
        if(!isLongPress()){
            mSelectedIndex = -1;
        }

        mKlineMaxValue = Float.MAX_VALUE;
        mKlineMinValue = Float.MIN_VALUE;
        mChildMaxValue = Float.MAX_VALUE;
        mChildMinValue = Float.MIN_VALUE;

        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));
        for (int i = mStartIndex; i <= mStopIndex; i++){
            KLineImpl point = (KLineImpl) getItem(i);
            if(mMainDraw != null){
                mKlineMaxValue = Math.max(mKlineMaxValue, mMainDraw.getMaxValue(point));
                mKlineMinValue = Math.min(mKlineMinValue, mMainDraw.getMinValue(point));
            }

            if(mChildDraw != null){
                mChildMaxValue = Math.max(mChildMaxValue, mChildDraw.getMaxValue(point));
                mChildMinValue = Math.min(mChildMinValue, mChildDraw.getMaxValue(point));
            }
        }

        float padding = (mKlineMaxValue - mKlineMinValue) * 0.05f;
        mKlineMaxValue += padding;
        mKlineMinValue -= padding;
        mMainScaleY = mKlineRect.height() * 1f / (mKlineMaxValue - mKlineMinValue);
        mChildScaleY = mChildRect.height() * 1f / (mChildMaxValue - mChildMinValue);

        if(mAnimator.isRunning()){
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }
    }

    public int indexOfTranslateX(float translateX){
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    /**
     * 二分法查找当前值得index
     * @param translateX
     * @param start
     * @param end
     * @return
     */
    public int indexOfTranslateX(float translateX, int start, int end){
        if(end == start){
            return start;
        }

        if(end - start == 1){
            float startValue = getX(start);
            float endValue = getX(end);
            return Math.abs(translateX - startValue) < Math.abs(mTranslateX - endValue) ? start : end;
        }

        int mid = start + (end - start) / 2;//中值下标
        float midValue = getX(mid);//获取二分法中值
        if(translateX < midValue){
            //左边查找
            return indexOfTranslateX(translateX, start, mid);
        }else if(translateX > midValue){
            //右边查找
            return indexOfTranslateX(translateX, mid, end);
        }else{
            //等于中值，中值下标
            return mid;
        }
    }

    /**
     * view中的x转化为TranslateX
     * @param x
     * @return
     */
    public float xToTranslateX(float x){
        return -mTranslateX + x / mScaleX;
    }

    public float translateXToX(float translateX){
        return (translateX + mTranslateX) * mScaleX;
    }

    /**
     * scrollX 转换为 TranslateX
     * @param scrollX
     */
    private void setTranslateXFromScrollX(int scrollX){
        mTranslateX = scrollX + getMinTranslateX();
    }

    public float getMainY(float value){
        return (mKlineMaxValue - value) * mMainScaleY + mKlineRect.top;
    }

    /**
     *
     * @param value
     * @return
     */
    public float getChildY(float value){
        return (mChildMaxValue - value) * mChildScaleY + mChildRect.top;
    }

    /**
     * 获取当前索引
     * @return
     */
    public int getSelectedIndex(){
        return mSelectedIndex;
    }

    /**
     * 获取子视图矩形对象
     * @return
     */
    public Rect getChildRect(){
        return mChildRect;
    }

    /**
     * 获取text画笔
     * @return
     */
    public Paint getTextPaint(){
        return mTextPaint;
    }

    /**
     * 获取选择先画笔
     * @return
     */
    public Paint getSelectedLinePaint(){
        return mSelectedLinePaint;
    }

    /**
     * 根据索引获取x坐标
     * @param position
     * @return
     */
    public float getX(int position){
        return position * mPointWidth;
    }

    /**
     * 设置每个点的宽度
     * @param pointWidth
     */
    public void setPointWidth(float pointWidth){
        mPointWidth = pointWidth;
    }

    /**
     * 设置背景画笔颜色
     * @param color
     */
    public void setBackGroundColor(int color){
        mBackGroundPaint.setColor(color);
    }

    /**
     * 获取背景画笔对象
     * @return
     */
    public Paint getBackGroundPaint(){
        return mBackGroundPaint;
    }

    /**
     * 设置十字选择先宽度
     * @param width
     */
    public void setSelectedLineWidth(float width){
        mSelectedLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置表格线颜色
     * @param color
     */
    public void setSelectedLineColor(int color){
        mSelectedLinePaint.setColor(color);
    }

    /**
     * 是否长按触摸
     * @return
     */
    public boolean isLongPress(){
        return isLongPress;
    }

    /**
     * 根据索引获取实体
     * @param position
     * @return
     */
    public Object getItem(int position){
        if(mAdapter != null){
            return mAdapter.getItem(position);
        }else{
            return null;
        }
    }

    /**
     * 获取主区域的 iChartDraw
     * @return
     */
    public IChartDraw getMainDraw(){
        return mMainDraw;
    }

    /**
     * 设置主区域的 IChartDraw
     * @param mainDraw
     */
    public void setMainDraw(IChartDraw mainDraw){
        mMainDraw = mainDraw;
    }

    /**
     * 设置子视图
     * @param position
     */
    public void setChildDraw(int position){
        this.mChildDraw = mChildDraws.get(position);
        mChildDrawPosition = position;
        invalidate();
    }

    /**
     * 给子区域添加画图方法
     * @param name
     * @param childDraw
     */
    public void addChildDraw(String name, IChartDraw childDraw){
        mChildDraws.add(childDraw);
        mKChartTabView.addTab(name);
    }

    /**
     * 设置数据适配器
     * @param adapter
     */
    public void setAdapter(IAdapter adapter){
        if(mAdapter != null && mDataSetObserver != null){
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        if(mAdapter != null){
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        }else{
            mItemCount = 0;
        }
        notifyChanged();
    }

    /**
     * 获取适配器
     * @return
     */
    public IAdapter getAdapter(){
        return mAdapter;
    }

    /**
     * 重新计算并刷新k线
     */
    public void notifyChanged(){
        if(mItemCount != 0){
            mDataLen = (mItemCount - 1) * mPointWidth;//数据长度
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);
        }else{
            setScrollX(0);
        }
        invalidate();
    }

    /**
     * 格式化值
     * @param value
     * @return
     */
    public String formatValue(float value){
        if(getValueFormatter() == null){
            setValueFormatter(new ValueFormatter());
        }
        return getValueFormatter().format(value);
    }

    /**
     * 获取IValueFormatter
     * @return
     */
    public IValueFormatter getValueFormatter(){
        return mValueFormatter;
    }

    /**
     * 设置IValueFormatter
     * @param valueFormatter value格式化器，指向实现类
     */
    public void setValueFormatter(IValueFormatter valueFormatter){
        this.mValueFormatter = valueFormatter;
    }

    /**
     * 获取格式化选择器
     * @return
     */
    public IDateTimeFormatter getDateTimeFormatter(){
        return mIDateTimeFormatter;
    }

    /**
     * 设置dateTimeFormatter
     * @param formatter
     */
    public void setDateTimeFormatter( IDateTimeFormatter formatter ){
        mIDateTimeFormatter = formatter;
    }

    /**
     * 格式化时间
     * @param date
     * @return
     */
    public String formatDateTime(Date date){
        if(mIDateTimeFormatter == null){
            setDateTimeFormatter(mIDateTimeFormatter);
        }
        return getDateTimeFormatter().format(date);
    }

    /**
     * 获取平移的最小值
     * @return
     */
    private float getMinTranslateX(){
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    private float getMaxTranslateX(){
        if(!isFullScreen()){
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    /**
     * 数据是否充满屏幕
     * @return
     */
    public boolean isFullScreen(){
        return mDataLen >=mWidth / mScaleX;
    }

    public float fixTextY(float y){
        Paint.FontMetrics fontMetrics =mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 -fontMetrics.descent);
    }

    @Override
    public int getMinScrollX() {
        return 0;
    }

    @Override
    public int getMaxScrollX() {
        return 0;
    }

    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }

    /**
     * 提供给外部的设置监听当前点变化的接口
     * @param l
     */
    public void setOnSelectedChangedListener(OnSelectedChangedListener l){
        this.onSelectedChangedListener = l;
    }

    /**
     * 当前点变化时的回调
     * @param view
     * @param point
     * @param index
     */
    public void onSelectedChanged(BaseKLineChartView view, Object point, int index){
        if(this.onSelectedChangedListener != null){
            onSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

}
