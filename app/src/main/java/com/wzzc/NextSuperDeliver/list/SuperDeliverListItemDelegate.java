package com.wzzc.NextSuperDeliver.list;

import com.wzzc.NextSuperDeliver.Production;

/**
 * Created by by Administrator on 2017/8/8.
 */

public interface SuperDeliverListItemDelegate {
    void buyGoods (String goodID);
    void getCoupons (Production production);
}
