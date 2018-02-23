package com.yilei.ownerdraw.view.KLineChart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.yilei.ownerdraw.R;
import com.yilei.ownerdraw.view.KLineChart.BaseKLineChartView;
import com.yilei.ownerdraw.view.KLineChart.base.IChartDraw;
import com.yilei.ownerdraw.view.KLineChart.base.IValueFormatter;
import com.yilei.ownerdraw.view.KLineChart.entityImpl.CandleImpl;
import com.yilei.ownerdraw.view.KLineChart.formatter.ValueFormatter;

/**
 * Created by 易磊 on 2018/2/8.
 * Candle的实现类， 画蜡烛图
 */

public class CandleDraw implements IChartDraw<CandleImpl>{
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma20Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mCandleWidth = 0;
    private float mCandleLineWidth = 0;

    private boolean mCandleSolid = true;
    private Context mContext;
    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CandleDraw(BaseKLineChartView view){
        Context context = view.getContext();
        mContext = context;
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));

    }

    @Override
    public void drawTranslated(@NonNull CandleImpl lastPoint, @NonNull CandleImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        //画蜡烛图
        drawCandle(view, canvas, curX, curPoint.getHighPrice(), curPoint.getLowPrice(), curPoint.getOpenPrice(), curPoint.getClosePrice());
        //画ma5线
        if(curPoint.getMA5Price() != 0){
            view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMA5Price(), curX, curPoint.getMA5Price());
        }
        //画ma10线
        if(curPoint.getMA10Price() != 0){
            view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getMA10Price(), curX, curPoint.getMA10Price());
        }
        //画ma20线
        if(curPoint.getMA20Price() != 0){
            view.drawMainLine(canvas, ma20Paint, lastX, lastPoint.getMA20Price(), curX, curPoint.getMA20Price());
        }
    }

    /**
     * 画蜡烛图
     * @param view
     * @param canvas
     * @param curX
     * @param highPrice
     * @param lowPrice
     * @param openPrice
     * @param closePrice
     */
    private void drawCandle(BaseKLineChartView view, Canvas canvas, float curX, float highPrice, float lowPrice, float openPrice, float closePrice){
        highPrice = view.getMainY(highPrice);
        lowPrice = view.getMainY(lowPrice);
        openPrice = view.getMainY(openPrice);
        closePrice = view.getMainY(closePrice);
        float r = mCandleWidth / 2;
        float lineR = mCandleLineWidth / 2;
        if(openPrice > closePrice){//涨
            if(mCandleSolid){//实心
                canvas.drawRect(curX - r, closePrice, curX + r, openPrice, mRedPaint);
                canvas.drawRect(curX - lineR, highPrice, curX + lineR, lowPrice, mRedPaint);
            }else{//空心 画线连接成矩形
                mRedPaint.setStrokeWidth(mCandleLineWidth);
                //画两段线连接起来
                canvas.drawLine(curX, highPrice, curX, closePrice, mRedPaint);
                canvas.drawLine(curX, openPrice, curX, lowPrice, mRedPaint);

                //x坐标不变，画垂直线
                canvas.drawLine(curX - r + lineR, openPrice, curX - r + lineR, closePrice, mRedPaint);
                canvas.drawLine(curX + r - lineR, openPrice, curX + r - lineR, closePrice, mRedPaint);

                mRedPaint.setStrokeWidth(mCandleLineWidth * view.getScaleX());
                //y坐标不变，画水平线
                canvas.drawLine(curX - r, openPrice, curX + r, openPrice, mRedPaint);
                canvas.drawLine(curX - r, closePrice, curX + r, openPrice, mRedPaint);


            }

        }else if(openPrice < closePrice){//跌
            canvas.drawRect(curX - r, openPrice, curX + r, closePrice, mGreenPaint);
            canvas.drawRect(curX - lineR, highPrice, curX + lineR, lowPrice, mGreenPaint);
        }else{//平
            canvas.drawRect(curX - r, openPrice, curX + r, closePrice + 1, mRedPaint);
            canvas.drawRect(curX - lineR, highPrice, curX + lineR, lowPrice, mRedPaint);
        }

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        CandleImpl point = (CandleImpl) view.getItem(position);
        String text = "";
        text = "MA5:" + view.formatValue(point.getMA5Price())+"";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "MA10:" + view.formatValue(point.getMA10Price())+"";
        canvas.drawText(text, x, y, ma10Paint);
        x += ma10Paint.measureText(text);
        text = "MA20:" + view.formatValue(point.getMA20Price())+"";
        canvas.drawText(text, x, y, ma20Paint);
        if(view.isLongPress()){
            drawSelector(view, canvas);//画长按的s十字及显示的矩形数据
        }
    }

    /**
     * draw选择器
     * @param view
     * @param canvas
     */
    private void drawSelector(BaseKLineChartView view, Canvas canvas){

    }

    @Override
    public float getMaxValue(CandleImpl point) {
        return Math.max(point.getHighPrice(), point.getMA20Price());
    }

    @Override
    public float getMinValue(CandleImpl point) {
        return Math.min(point.getMA20Price(), point.getLowPrice());
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 设置蜡烛线宽度
     * @param candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth){
        mCandleLineWidth = candleLineWidth;
    }

    /**
     * 设置蜡烛宽度
     * @param candleWidth
     */
    public void setCandleWidth(float candleWidth){
        mCandleWidth = candleWidth;
    }

    /**
     * 设置ma5颜色
     * @param color
     */
    public void setMa5Color(int color){
        ma5Paint.setColor(color);
    }

    /**
     * 设置ma10颜色
     * @param color
     */
    public void setMa10Color(int color){
        ma10Paint.setColor(color);
    }

    /**
     * 设置ma20颜色
     * @param color
     */
    public void setMa20Color(int color){
        ma20Paint.setColor(color);
    }

    /**
     * 设置曲线宽度
     * @param width
     */
    public void setLineWidth(float width){
        ma5Paint.setStrokeWidth(width);
        ma10Paint.setStrokeWidth(width);
        ma20Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     * @param textSize
     */
    public void setTextSize(float textSize){
        ma5Paint.setTextSize(textSize);
        ma10Paint.setTextSize(textSize);
        ma20Paint.setTextSize(textSize);
    }



    /**
     * 设置蜡烛是否是实心
     * @param candleSolid
     */
    public void setCandleSolid(boolean candleSolid){
        this.mCandleSolid = candleSolid;
    }


}
