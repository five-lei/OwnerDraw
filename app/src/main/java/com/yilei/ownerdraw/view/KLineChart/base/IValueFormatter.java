package com.yilei.ownerdraw.view.KLineChart.base;

/**
 * Created by 易磊 on 2018/2/7.
 * value格式化接口
 */

public interface IValueFormatter {

    /**
     * 格式化value
     * @param value 传入的value值
     * @return 返回字符串
     */
    String format(float value);
}
