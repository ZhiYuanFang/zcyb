package com.wzzc.onePurchase.activity.index.main_view.shopCar;

import android.content.Context;
import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.shopCar.main_view.ShopCarView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/29.
 */

public class OnePurchaseShopCarActivity extends BaseActivity {
    @ViewInject(R.id.basicLayout)
    private OnePurchasePanelBasicLayoutView basicLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(this.getString(R.string.shopCar));
        ShopCarView shopCarView = new ShopCarView(this);
        shopCarView.setInfo(null);
        basicLayout.setInfo(onePurchasePanelBasicTitleView,shopCarView);
    }


}
