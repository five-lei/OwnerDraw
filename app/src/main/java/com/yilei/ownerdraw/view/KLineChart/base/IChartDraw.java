package com.yilei.ownerdraw.view.KLineChart.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.yilei.ownerdraw.view.KLineChart.BaseKLineChartView;

/**
 * Created by 易磊 on 2018/2/7.
 * 画图的基类接口，定义画图的基本方法
 * 根据传入的泛型接口画不同的k线图
 */

public interface IChartDraw<T> {

    /**
     * 需要滑动物体draw方法
     * @param lastPoint 上一个点
     * @param curPoint 当前点
     * @param lastX 上一个点的x坐标
     * @param curX 当前点的X坐标
     * @param canvas
     * @param view k线图View
     * @param position 当前点的位置
     */
    void drawTranslated(@NonNull T lastPoint, @NonNull T curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position);

    /**
     *
     * @param canvas
     * @param view
     * @param position 该点的位置
     * @param x x的起始坐标
     * @param y y的起始坐标
     */
    void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y);

    /**
     * 获取当前实体中最大的值
     * @param point
     * @return
     */
    float getMaxValue(T point);

    /**
     * 获取当前实体中最小的值
     * @param point
     * @return
     */
    float getMinValue(T point);

    /**
     * 获取Value格式化器
     * @return
     */
    IValueFormatter getValueFormatter();
}
