package com.wzzc.onePurchase.activity.index.main_view.center.activity.myShowOrder;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.myShowOrder.main_view.OnePurchaseMyShowOrderView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/31.
 *
 * 我的晒单
 */

public class OnePurchaseMyShowOrderActivity extends BaseActivity {

    public static final String RECID = "rec_id";
    @ViewInject(R.id.basicLayout)
    private OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        JSONObject sender = null;
        try {
            sender = new JSONObject(String.valueOf(bundle.get("info")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(getString(R.string.myShowOrder));
        OnePurchaseMyShowOrderView onePurchaseMyShowOrderView = new OnePurchaseMyShowOrderView(this);
        onePurchaseMyShowOrderView.setInfo(sender);
        onePurchasePanelBasicLayoutView.setInfo(onePurchasePanelBasicTitleView,onePurchaseMyShowOrderView);

    }
}
