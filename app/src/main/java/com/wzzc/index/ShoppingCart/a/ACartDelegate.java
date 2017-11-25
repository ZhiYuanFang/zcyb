package com.wzzc.index.ShoppingCart.a;

/**
 * Created by by Administrator on 2017/6/16.
 */

public interface ACartDelegate {
    /**
     * @param changeMoney           是否改变结算金额
     * @param changeNumber          是否改变结算数量
     * @param delete                是否删除
     * @param add                   是否为添加入结算 false : 从结算扣除 true : 从结算基础添加
     * @param rec_id                商品所在购物车ID
     * @param itemNumber            改变数量
     * @param goods_price_original  当前价格
     * @param market_price_original 超市价格
     */
    void change(boolean changeMoney, boolean changeNumber, boolean delete, boolean add, String rec_id, int itemNumber, String goods_price_original, String market_price_original);
}
