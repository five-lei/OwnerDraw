package com.yilei.ownerdraw.view.KLineChart.base;

import com.yilei.ownerdraw.view.KLineChart.BaseKLineChartView;

/**
 * Created by 易磊 on 2018/2/9.
 * 选中点的监听
 */

public interface OnSelectedChangedListener {

    /**
     * 选中变化时的回调
     * @param view
     * @param point
     * @param index
     */
    void onSelectedChanged(BaseKLineChartView view, Object point, int index);

}
