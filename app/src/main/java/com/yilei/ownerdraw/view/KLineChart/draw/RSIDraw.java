package com.yilei.ownerdraw.view.KLineChart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.yilei.ownerdraw.view.KLineChart.BaseKLineChartView;
import com.yilei.ownerdraw.view.KLineChart.base.IChartDraw;
import com.yilei.ownerdraw.view.KLineChart.base.IValueFormatter;
import com.yilei.ownerdraw.view.KLineChart.entityImpl.RSIImpl;
import com.yilei.ownerdraw.view.KLineChart.formatter.ValueFormatter;

/**
 * Created by 易磊 on 2018/2/8.
 * RSI实现类， 画RSI图
 */

public class RSIDraw implements IChartDraw<RSIImpl>{
    private Paint mRSI1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI3Paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public RSIDraw(BaseKLineChartView view){

    }
    @Override
    public void drawTranslated(@NonNull RSIImpl lastPoint, @NonNull RSIImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        view.drawChildLine(canvas, mRSI1Paint, lastX, lastPoint.getRsi1(), curX, curPoint.getRsi1());
        view.drawChildLine(canvas, mRSI2Paint, lastX, lastPoint.getRsi2(), curX, curPoint.getRsi2());
        view.drawChildLine(canvas, mRSI3Paint, lastX, lastPoint.getRsi3(), curX, curPoint.getRsi3());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        RSIImpl point = (RSIImpl) view.getItem(position);
        String text = "";
        text = "RSI1:" + view.formatValue(point.getRsi1())+"";
        canvas.drawText(text, x, y, mRSI1Paint);
        x += mRSI1Paint.measureText(text);
        text = "RSI2:" + view.formatValue(point.getRsi2())+"";
        canvas.drawText(text, x, y, mRSI2Paint);
        x += mRSI2Paint.measureText(text);
        text = "RSI3:" + view.formatValue(point.getRsi3())+"";
        canvas.drawText(text, x, y, mRSI3Paint);
    }

    @Override
    public float getMaxValue(RSIImpl point) {
        return Math.max(point.getRsi1(), Math.max(point.getRsi2(), point.getRsi3()));
    }

    @Override
    public float getMinValue(RSIImpl point) {
        return Math.min(point.getRsi1(), Math.min(point.getRsi2(), point.getRsi3()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 设置RSI1颜色
     * @param color
     */
    public void setRSI1Color(int color){
        mRSI1Paint.setColor(color);
    }

    /**
     * 设置RSI2颜色
     * @param color
     */
    public void setRSI2Color(int color){
        mRSI2Paint.setColor(color);
    }

    /**
     * 设置RSI3颜色
     * @param color
     */
    public void setRSI3Color(int color){
        mRSI3Paint.setColor(color);
    }

    /**
     * 设置曲线宽度
     * @param width
     */
    public void setLineWidth(float width){
        mRSI1Paint.setStrokeWidth(width);
        mRSI2Paint.setStrokeWidth(width);
        mRSI3Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字字体大小
     * @param textSize
     */
    public void setTextSize(float textSize){
        mRSI1Paint.setTextSize(textSize);
        mRSI2Paint.setTextSize(textSize);
        mRSI3Paint.setTextSize(textSize);
    }
}
