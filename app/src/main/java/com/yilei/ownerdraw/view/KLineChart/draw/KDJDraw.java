package com.yilei.ownerdraw.view.KLineChart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.yilei.ownerdraw.view.KLineChart.BaseKLineChartView;
import com.yilei.ownerdraw.view.KLineChart.base.IChartDraw;
import com.yilei.ownerdraw.view.KLineChart.base.IValueFormatter;
import com.yilei.ownerdraw.view.KLineChart.entityImpl.KDJImpl;
import com.yilei.ownerdraw.view.KLineChart.formatter.ValueFormatter;

/**
 * Created by 易磊 on 2018/2/7.
 * KDJ实现类， 画KDJ图
 */

public class KDJDraw implements IChartDraw<KDJImpl>{

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public KDJDraw(BaseKLineChartView view){

    }

    /**
     * 需要滑动物体视图draw方法
     * @param lastPoint
     * @param curPoint
     * @param lastX
     * @param curX
     * @param canvas
     * @param view
     * @param position
     */
    @Override
    public void drawTranslated(@NonNull KDJImpl lastPoint, @NonNull KDJImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        view.drawChildLine(canvas, mKPaint, lastX, lastPoint.getK(), curX, curPoint.getK());
        view.drawChildLine(canvas, mDPaint, lastX, lastPoint.getD(), curX, curPoint.getD());
        view.drawChildLine(canvas, mJPaint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        String text = "";
        KDJImpl point = (KDJImpl) view.getItem(position);
        text = "K:" + view.formatValue(point.getK())+"";
        canvas.drawText(text, x, y, mKPaint);
        x += mKPaint.measureText(text);
        text = "D:" + view.formatValue(point.getD())+"";
        canvas.drawText(text, x, y, mDPaint);
        x += mDPaint.measureText(text);
        text = "J:" + view.formatValue(point.getJ())+"";
        canvas.drawText(text, x, y, mJPaint);
    }

    @Override
    public float getMaxValue(KDJImpl point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(KDJImpl point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 设置k的颜色
     * @param color
     */
    public void setKColor(int color){
        mKPaint.setColor(color);
    }

    /**
     * 设置D的颜色
     * @param color
     */
    public void setDColor(int color){
        mDPaint.setColor(color);
    }

    /**
     * 设置J的颜色
     * @param color
     */
    public void setJColor(int color){
        mJPaint.setColor(color);
    }

    /**
     * 设置曲线的宽度
     * @param width
     */
    public void setLineWidth(float width){
        mKPaint.setStrokeWidth(width);
        mDPaint.setStrokeWidth(width);
        mJPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字字体大小
     * @param textSize
     */
    public void setTextSize(float textSize){
        mKPaint.setTextSize(textSize);
        mDPaint.setTextSize(textSize);
        mJPaint.setTextSize(textSize);
    }
}
