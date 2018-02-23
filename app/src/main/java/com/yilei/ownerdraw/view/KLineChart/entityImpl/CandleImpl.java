package com.yilei.ownerdraw.view.KLineChart.entityImpl;

/**
 * Created by 易磊 on 2018/2/7.
 * 蜡烛图
 */

public interface CandleImpl {

    /**
     * 开盘价
     * @return
     */
    public float getOpenPrice();

    /**
     * 最高价
     * @return
     */
    public float getHighPrice();

    /**
     * 最低价
     * @return
     */
    public float getLowPrice();

    /**
     * 收盘价
     * @return
     */
    public float getClosePrice();

    public float getMA5Price();

    public float getMA10Price();

    public float getMA20Price();
}
