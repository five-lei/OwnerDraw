package com.yilei.ownerdraw.view.KLineChart.base;

import android.database.DataSetObserver;

import java.util.Date;

/**
 * Created by 易磊 on 2018/2/7.
 * 数据适配器
 */

public interface IAdapter {

    /**
     * 获取点的数目
     * @return
     */
    int getCount();

    /**
     * 根据序号获取item
     * @param position
     * @return 数据实体
     */
    Object getItem(int position);

    /**
     * 根据序号获取时间
     * @param position
     * @return
     */
    Date getDate(int position);

    /**
     *注册一个数据观察者
     * @param observer 数据观察者
     */
    void registerDataSetObserver(DataSetObserver observer);

    /**
     * 移除一个数据观察者
     * @param observer
     */
    void unregisterDataSetObserver(DataSetObserver observer);

    /**
     * 当数据发生变化时调用
     */
    void notifyDataSetChanged();
}
