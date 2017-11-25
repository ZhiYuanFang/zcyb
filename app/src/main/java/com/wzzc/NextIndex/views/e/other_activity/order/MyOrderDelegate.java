package com.wzzc.NextIndex.views.e.other_activity.order;

/**
 * Created by by Administrator on 2017/6/22.
 */

public interface MyOrderDelegate {
    void confirmOrder(String order_id);//确认收货
    void lookOrderDetail (String order_id , int isOld) ;//查看订单详情 ; isOld : 1 -> 历史订单  0 -> 普通订单
    void cancelOrder (String order_id) ;//取消订单
}
