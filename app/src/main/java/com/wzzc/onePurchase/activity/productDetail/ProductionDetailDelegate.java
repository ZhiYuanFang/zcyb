package com.wzzc.onePurchase.activity.productDetail;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/3/23.
 */

public interface ProductionDetailDelegate {
    /** 当点击<图片...>,获取数据后 通过该委托通知更新数据*/
    void refreshInfoProfile (JSONObject sender);
}
