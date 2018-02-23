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
import com.yilei.ownerdraw.view.KLineChart.entityImpl.MACDImpl;
import com.yilei.ownerdraw.view.KLineChart.formatter.ValueFormatter;

/**
 * Created by admin on 2018/2/7.
 * MACD实现类， 画MACD图
 */

public class MACDDraw implements IChartDraw<MACDImpl>{
    private Paint mRedPain = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**macd 中柱子的宽度*/
    private float mMACDWidth = 0;

    public MACDDraw(BaseKLineChartView view){
        Context context = view.getContext();
        mRedPain.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
    }

    @Override
    public void drawTranslated(@NonNull MACDImpl lastPoint, @NonNull MACDImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        drawMACD(canvas, view, curX, curPoint.getMacd());
        view.drawChildLine(canvas, mDIFPaint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
        view.drawChildLine(canvas, mDEAPaint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        MACDImpl point = (MACDImpl) view.getItem(position);
        String text = "";
        text = "DIF:" + view.formatValue(point.getDif())+"";
        canvas.drawText(text, x, y, mDIFPaint);
        x += mDIFPaint.measureText(text);
        text = "DEA:" + view.formatValue(point.getDea())+"";
        canvas.drawText(text, x, y, mDEAPaint);
        x += mDEAPaint.measureText(text);
        text = "MACD:" + view.formatValue(point.getMacd());
        canvas.drawText(text, x, y, mMACDPaint);
    }

    @Override
    public float getMaxValue(MACDImpl point) {
        return Math.max(point.getMacd(), Math.max(point.getDea(), point.getDif()));
    }

    @Override
    public float getMinValue(MACDImpl point) {
        return Math.min(point.getMacd(), Math.min(point.getDea(), point.getDif()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 设置DIF的颜色
     * @param color
     */
    public void setDIFColor(int color){
        mDIFPaint.setColor(color);
    }

    /**
     * 设置DEA的颜色
     * @param color
     */
    public void setDEAColor(int color){
        mDEAPaint.setColor(color);
    }

    public void drawMACD(Canvas canvas, BaseKLineChartView view, float x, float macd){
        float macdY = view.getChildY(macd);
        float r = mMACDWidth / 2;
        float zeroY = view.getChildY(0);
        if(macd > 0){
            canvas.drawRect(x - r, macdY, x + r, zeroY, mMACDPaint);
        }else{
            canvas.drawRect(x - r, zeroY, x + r, macdY, mMACDPaint);
        }

    }

    /**
     * 设置macd柱子的宽度
     * @param width
     */
    public void setMACDWidth(float width){
        this.mMACDWidth = width;
    }

    /**
     * 设置曲线宽度
     * @param width
     */
    public void setLineWidth(float width){
        mDEAPaint.setStrokeWidth(width);
        mDIFPaint.setStrokeWidth(width);
        mMACDPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     * @param textSize
     */
    public void setTextSize(float textSize){
        mDEAPaint.setTextSize(textSize);
        mDIFPaint.setTextSize(textSize);
        mMACDPaint.setTextSize(textSize);
    }
}
