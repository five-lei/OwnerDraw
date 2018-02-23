package com.yilei.ownerdraw.view.KLineChart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.yilei.ownerdraw.view.KLineChart.BaseKLineChartView;
import com.yilei.ownerdraw.view.KLineChart.base.IChartDraw;
import com.yilei.ownerdraw.view.KLineChart.base.IValueFormatter;
import com.yilei.ownerdraw.view.KLineChart.entityImpl.BOLLImpl;
import com.yilei.ownerdraw.view.KLineChart.formatter.ValueFormatter;

/**
 * Created by 易磊 on 2018/2/7.
 * BOLL实现类， 画BOLL图
 */

public class BOLLDraw implements IChartDraw<BOLLImpl>{
    private Paint mUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BOLLDraw(BaseKLineChartView view){

    }

    @Override
    public void drawTranslated(@NonNull BOLLImpl lastPoint, @NonNull BOLLImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        view.drawChildLine(canvas, mUpPaint, lastX, lastPoint.getUp(), curX, curPoint.getUp());//画up线
        view.drawChildLine(canvas, mMbPaint, lastX, lastPoint.getMb(), curX, curPoint.getMb());//画mb线
        view.drawChildLine(canvas, mDnPaint, lastX, lastPoint.getDn(), curX, curPoint.getDn());//画dn线
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        String text = "";
        BOLLImpl point = (BOLLImpl) view.getItem(position);
        text = "UP:" + view.formatValue(point.getUp())+"";
        canvas.drawText(text, x, y, mUpPaint);
        x += mUpPaint.measureText(text);
        text = "MB:" + view.formatValue(point.getMb())+"";
        canvas.drawText(text, x, y, mMbPaint);
        x += mMbPaint.measureText(text);
        text = "DN:" + view.formatValue(point.getDn())+"";
        canvas.drawText(text, x, y, mDnPaint);
    }

    @Override
    public float getMaxValue(BOLLImpl point) {
        if(Float.isNaN(point.getUp())){
            return point.getMb();
        }
        return point.getUp();
    }

    @Override
    public float getMinValue(BOLLImpl point) {
        if(Float.isNaN(point.getDn())){
            return point.getMb();
        }
        return point.getDn();
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 设置up颜色
     * @param color
     */
    public void setUpColor(int color){
        mUpPaint.setColor(color);
    }

    /**
     * 设置Mb颜色
     * @param color
     */
    public void setMbColor(int color){
        mMbPaint.setColor(color);
    }

    /**
     * 设置Dn颜色
     * @param color
     */
    public void setDnColor(int color){
        mDnPaint.setColor(color);
    }

    /**
     *设置曲线宽度
     * @param width
     */
    public void setLineWidth(float width){
        mUpPaint.setStrokeWidth(width);
        mMbPaint.setStrokeWidth(width);
        mDnPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     * @param textSize
     */
    public void setTextSize(float textSize){
        mUpPaint.setTextSize(textSize);
        mMbPaint.setTextSize(textSize);
        mUpPaint.setTextSize(textSize);
    }
}
