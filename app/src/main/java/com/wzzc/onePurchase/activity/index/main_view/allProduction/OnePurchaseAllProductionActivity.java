package com.wzzc.onePurchase.activity.index.main_view.allProduction;

import android.content.Context;
import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.allProduction.main_view.OnePurchaseProductionView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/29.
 *
 * 所有商品
 */

public class OnePurchaseAllProductionActivity extends BaseActivity {
    @ViewInject(R.id.basicLayout)
    private OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init (){
        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(this.getString(R.string.allProduction));

        OnePurchaseProductionView onePurchaseProductionView = new OnePurchaseProductionView(this);
        onePurchaseProductionView.setInfo();
        onePurchasePanelBasicLayoutView.setInfo(onePurchasePanelBasicTitleView,onePurchaseProductionView);
    }
}
