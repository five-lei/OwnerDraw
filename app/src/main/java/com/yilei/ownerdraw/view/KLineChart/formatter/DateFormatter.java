package com.yilei.ownerdraw.view.KLineChart.formatter;

import com.yilei.ownerdraw.view.KLineChart.base.IDateTimeFormatter;
import com.yilei.ownerdraw.view.KLineChart.util.DateUtil;

import java.util.Date;

/**
 * Created by 易磊 on 2018/2/11.
 */

public class DateFormatter implements IDateTimeFormatter{
    @Override
    public String format(Date date) {
        if(date != null){
            return DateUtil.DateFormat.format(date);
        }else{
            return "";
        }
    }
}
