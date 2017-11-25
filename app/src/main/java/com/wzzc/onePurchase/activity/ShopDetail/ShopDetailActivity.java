package com.wzzc.onePurchase.activity.ShopDetail;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.ShopDetail.mainView.ShopDetailMainView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/24.
 *
 * 云购详情
 */

public class ShopDetailActivity extends BaseActivity {

    @ViewInject(R.id.basicLayout)
    OnePurchasePanelBasicLayoutView basicLayoutView;
    ShopDetailMainView mainView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnePurchasePanelBasicTitleView titleView = new OnePurchasePanelBasicTitleView(this);
        titleView.setTv_title(getString(R.string.cloudShopDetail));
        mainView = new ShopDetailMainView(this);
        basicLayoutView.setInfo(titleView,mainView);
        getServerInfo();
    }

    private void getServerInfo () {
        JSONObject sender = null;
        mainView.setInfo(sender);
    }
}
