package com.yilei.ownerdraw.view.KLineChart.util;

import android.content.Context;

/**
 * Created by yilei on 2018/2/6.
 * px和dp根据分辨率的转化
 */

public class ViewUtil {
    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale +0.5f);
    }

    /**
     * px转dp
     * @param context
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
