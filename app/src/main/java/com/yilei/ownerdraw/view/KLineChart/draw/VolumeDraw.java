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
import com.yilei.ownerdraw.view.KLineChart.entityImpl.VolumeImpl;
import com.yilei.ownerdraw.view.KLineChart.formatter.ValueFormatter;
import com.yilei.ownerdraw.view.KLineChart.util.ViewUtil;

/**
 * Created by 易磊 on 2018/2/8.
 * Volume实现类 画Volume图
 */

public class VolumeDraw implements IChartDraw<VolumeImpl>{
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int pillarWidth = 0;//柱子的宽度

    public VolumeDraw(BaseKLineChartView view){
        Context context = view.getContext();
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
        pillarWidth = ViewUtil.Dp2Px(context, 4);
    }

    @Override
    public void drawTranslated(@NonNull VolumeImpl lastPoint, @NonNull VolumeImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        //画柱状图
        drawHistogram(canvas, curPoint, lastPoint, curX, view, position);
        //画5交易均线
        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getMA5Volume(), curX, curPoint.getMA5Volume());
        //画10交易均线
        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getMA10Volume(), curX, curPoint.getMA10Volume());

    }

    /**
     * 画柱状图
     * @param canvas
     * @param curPoint
     * @param latPoint
     * @param curX
     * @param view
     * @param position
     */
    private void drawHistogram(Canvas canvas, VolumeImpl curPoint, VolumeImpl latPoint, float curX, BaseKLineChartView view, int position){
        float r = pillarWidth / 2;
        float top = view.getChildY(curPoint.getVolume());
        int bottom = view.getChildRect().bottom;
        if(curPoint.getClosePrice() >= curPoint.getOpenPrice()){
            //收盘大于等于开盘，，涨
            canvas.drawRect(curX - r, top, curX + r, bottom, mRedPaint);
        }else{
            //跌
            canvas.drawRect(curX - r, top, curX + r, bottom, mGreenPaint);
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        String text = "";
        VolumeImpl point = (VolumeImpl) view.getItem(position);
        text = "VOL:" + view.formatValue(point.getVolume())+"";
        canvas.drawText(text, x, y, view.getTextPaint());
        x += view.getTextPaint().measureText(text);
        text = "MA5:" + view.formatValue(point.getMA5Volume())+"";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "MA10:" + view.formatValue(point.getMA10Volume())+"";
        canvas.drawText(text, x, y, ma10Paint);
    }

    @Override
    public float getMaxValue(VolumeImpl point) {
        return Math.max(point.getVolume(), Math.max(point.getMA5Volume(), point.getMA10Volume()));
    }

    @Override
    public float getMinValue(VolumeImpl point) {
        return Math.min(point.getVolume(), Math.min(point.getMA5Volume(), point.getMA10Volume()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 设置MA5颜色
     * @param color
     */
    public void setMA5Color(int color){
        ma5Paint.setColor(color);
    }

    /**
     * 设置MA10颜色
     * @param color
     */
    public void setMA10Color(int color){
        ma10Paint.setColor(color);
    }

    /**
     * 设置曲线宽度
     * @param width
     */
    public void setLineWidth(float width){
        ma5Paint.setStrokeWidth(width);
        ma10Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字字体大小
     * @param textSize
     */
    public void setTextSize(float textSize){
        ma5Paint.setTextSize(textSize);
        ma10Paint.setTextSize(textSize);
    }
}
