package com.yilei.ownerdraw.view.KLineChart.formatter;

import com.yilei.ownerdraw.view.KLineChart.base.IValueFormatter;

/**
 * Created by 易磊 on 2018/2/7.
 * IValueFormatter接口的实现类
 * Value格式化类
 */

public class ValueFormatter implements IValueFormatter{
    @Override
    public String format(float value) {
        //保留6位小数
        return String.format("%.6f", value);
    }
}
